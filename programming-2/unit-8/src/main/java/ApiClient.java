import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all communication with the OpenWeatherMap API.
 *
 * Two endpoints are used:
 * <ul>
 * <li>/weather — current weather for a city</li>
 * <li>/forecast — 3-hour forecast slots (up to 40 entries, 5 days)</li>
 * </ul>
 *
 * JSON is parsed manually using String operations so that no external
 * library is required. The helper method {@link #extract} locates a
 * key inside a flat JSON string and returns its raw value token.
 *
 * All network calls are synchronous and must be called from a
 * background thread (JavaFX Task) to avoid blocking the UI thread.
 *
 * @author Hee Moon
 */
public class ApiClient {

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5";
    private static final int TIMEOUT_MS = 8000;

    /** Number of forecast slots shown in the UI (each slot = 3 hours). */
    private static final int FORECAST_SLOTS = 5;

    private final String apiKey;

    /**
     * Creates an ApiClient with the given OpenWeatherMap API key.
     *
     * @param apiKey valid OpenWeatherMap API key
     */
    public ApiClient(String apiKey) {
        this.apiKey = apiKey;
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Fetches current weather for the named city.
     *
     * @param city city name (e.g. "Seoul", "London")
     * @return populated WeatherData object
     * @throws Exception if the network request fails or the city is not found
     */
    public WeatherData fetchCurrentWeather(String city) throws Exception {
        String encoded = URLEncoder.encode(city.trim(), StandardCharsets.UTF_8);
        String urlStr = BASE_URL + "/weather?q=" + encoded
                + "&appid=" + apiKey + "&units=metric";

        String json = get(urlStr);
        return parseWeather(json);
    }

    /**
     * Fetches a short-term forecast for the named city.
     * Returns up to {@value #FORECAST_SLOTS} 3-hour slots.
     *
     * @param city city name
     * @return list of ForecastData objects, earliest slot first
     * @throws Exception if the network request fails or the city is not found
     */
    public List<ForecastData> fetchForecast(String city) throws Exception {
        String encoded = URLEncoder.encode(city.trim(), StandardCharsets.UTF_8);
        String urlStr = BASE_URL + "/forecast?q=" + encoded
                + "&appid=" + apiKey + "&units=metric"
                + "&cnt=" + FORECAST_SLOTS;

        String json = get(urlStr);
        return parseForecast(json);
    }

    // -------------------------------------------------------------------------
    // HTTP
    // -------------------------------------------------------------------------

    /**
     * Performs a GET request and returns the response body as a String.
     *
     * @param urlStr fully formed URL string
     * @return response body text
     * @throws Exception on HTTP error or I/O failure
     */
    private String get(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(TIMEOUT_MS);
        conn.setReadTimeout(TIMEOUT_MS);
        conn.setRequestProperty("Accept", "application/json");

        int status = conn.getResponseCode();

        // Read either the success stream or the error stream.
        BufferedReader reader;
        if (status >= 200 && status < 300) {
            reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        } else {
            reader = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        conn.disconnect();

        String body = sb.toString();

        // Surface API-level errors (e.g. city not found → cod:404).
        if (status != 200) {
            String message = extract(body, "message");
            throw new Exception("API error " + status + ": "
                    + (message.isEmpty() ? body : message));
        }

        return body;
    }

    // -------------------------------------------------------------------------
    // Parsing — current weather
    // -------------------------------------------------------------------------

    /**
     * Parses an OpenWeatherMap /weather JSON response into a WeatherData object.
     *
     * @param json raw JSON string from the API
     * @return populated WeatherData
     */
    private WeatherData parseWeather(String json) {
        String city = extract(json, "name");
        String country = extract(json, "country");
        double temp = toDouble(extract(json, "temp"));
        double feelsLike = toDouble(extract(json, "feels_like"));
        int humidity = (int) toDouble(extract(json, "humidity"));
        double windSpeed = toDouble(extract(json, "speed"));
        String condition = extractFromArray(json, "main");
        String description = extractFromArray(json, "description");
        String iconCode = extractFromArray(json, "icon");
        int pressure = (int) toDouble(extract(json, "pressure"));
        int visibility = (int) toDouble(extract(json, "visibility"));

        return new WeatherData(city, country, temp, feelsLike,
                humidity, windSpeed, condition, description,
                iconCode, pressure, visibility);
    }

    // -------------------------------------------------------------------------
    // Parsing — forecast
    // -------------------------------------------------------------------------

    /**
     * Parses an OpenWeatherMap /forecast JSON response.
     *
     * The response contains a "list" array; each element represents
     * one 3-hour slot. We extract the first {@value #FORECAST_SLOTS} slots.
     *
     * @param json raw JSON string from the API
     * @return list of ForecastData objects
     */
    private List<ForecastData> parseForecast(String json) {
        List<ForecastData> result = new ArrayList<>();

        // Locate the "list" array.
        int listStart = json.indexOf("\"list\"");
        if (listStart == -1)
            return result;

        int arrayStart = json.indexOf('[', listStart);
        if (arrayStart == -1)
            return result;

        // Split into individual slot objects by finding top-level '{' ... '}'.
        int pos = arrayStart + 1;
        int depth = 0;
        int slotStart = -1;

        while (pos < json.length() && result.size() < FORECAST_SLOTS) {
            char c = json.charAt(pos);
            if (c == '{') {
                if (depth == 0)
                    slotStart = pos;
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && slotStart != -1) {
                    String slot = json.substring(slotStart, pos + 1);
                    result.add(parseSlot(slot));
                    slotStart = -1;
                }
            }
            pos++;
        }

        return result;
    }

    /**
     * Parses a single forecast slot JSON object.
     *
     * @param slot JSON fragment for one 3-hour slot
     * @return ForecastData for that slot
     */
    private ForecastData parseSlot(String slot) {
        // "dt_txt":"2024-05-01 15:00:00" → extract "15:00" as label
        String dtTxt = extract(slot, "dt_txt");
        String timeLabel = dtTxt.length() >= 16 ? dtTxt.substring(11, 16) : dtTxt;

        double temp = toDouble(extract(slot, "temp"));
        String cond = extractFromArray(slot, "main");
        String iconCode = extractFromArray(slot, "icon");

        return new ForecastData(timeLabel, temp, cond, iconCode);
    }

    // -------------------------------------------------------------------------
    // JSON helpers
    // -------------------------------------------------------------------------

    /**
     * Extracts the value of a JSON key from a flat (non-nested) context.
     *
     * Searches for {@code "key":value} and returns the raw value token
     * (string content without quotes, or the bare number/boolean).
     * Returns an empty string if the key is not found.
     *
     * @param json JSON string to search
     * @param key  key name to look up
     * @return raw value string, or "" if not found
     */
    private String extract(String json, String key) {
        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx == -1)
            return "";

        int colon = json.indexOf(':', idx + search.length());
        if (colon == -1)
            return "";

        // Skip whitespace after the colon.
        int start = colon + 1;
        while (start < json.length() && json.charAt(start) == ' ')
            start++;

        if (start >= json.length())
            return "";

        if (json.charAt(start) == '"') {
            // String value — find the closing quote, respecting escaped quotes.
            int end = start + 1;
            while (end < json.length()) {
                if (json.charAt(end) == '"' && json.charAt(end - 1) != '\\')
                    break;
                end++;
            }
            return json.substring(start + 1, end);
        } else {
            // Number / boolean / null — ends at ',', '}', or ']'.
            int end = start;
            while (end < json.length()) {
                char c = json.charAt(end);
                if (c == ',' || c == '}' || c == ']')
                    break;
                end++;
            }
            return json.substring(start, end).trim();
        }
    }

    /**
     * Extracts a value from the first object inside a JSON array field.
     *
     * OpenWeatherMap wraps condition info in an array:
     * {@code "weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01d"}]}
     * This method finds the first {@code {...}} block after the array opening
     * and then calls {@link #extract} on it.
     *
     * @param json JSON string to search
     * @param key  key inside the first array element
     * @return extracted value, or "" if not found
     */
    private String extractFromArray(String json, String key) {
        // Find the first '[' and then the first '{' inside it.
        int bracket = json.indexOf('[');
        if (bracket == -1)
            return extract(json, key);

        int brace = json.indexOf('{', bracket);
        if (brace == -1)
            return "";

        // Find the matching closing '}'.
        int depth = 0;
        int end = brace;
        while (end < json.length()) {
            if (json.charAt(end) == '{')
                depth++;
            else if (json.charAt(end) == '}') {
                depth--;
                if (depth == 0)
                    break;
            }
            end++;
        }

        String firstElement = json.substring(brace, end + 1);
        return extract(firstElement, key);
    }

    /**
     * Parses a string token as a double, returning 0.0 on failure.
     *
     * @param s string representation of a number
     * @return parsed double, or 0.0 if the string is empty or malformed
     */
    private double toDouble(String s) {
        if (s == null || s.isEmpty())
            return 0.0;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}