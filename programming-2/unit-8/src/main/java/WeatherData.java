/**
 * Holds current weather data for a single location.
 *
 * This is a plain data class (no logic) whose fields are populated
 * by ApiClient after parsing the OpenWeatherMap JSON response.
 *
 * @author Hee Moon
 */
public class WeatherData {

    /** City name returned by the API (may differ from user input). */
    public final String city;

    /** Country code, e.g. "KR", "US". */
    public final String country;

    /** Temperature in Celsius. */
    public final double tempCelsius;

    /** Perceived temperature in Celsius. */
    public final double feelsLikeCelsius;

    /** Humidity percentage (0–100). */
    public final int humidity;

    /** Wind speed in metres per second. */
    public final double windSpeedMs;

    /** Short weather condition label, e.g. "Clear", "Rain". */
    public final String condition;

    /** Longer description, e.g. "light rain", "clear sky". */
    public final String description;

    /**
     * OpenWeatherMap icon code (e.g. "01d", "10n").
     * Used by WeatherPanel to select the matching icon label.
     */
    public final String iconCode;

    /** Atmospheric pressure in hPa. */
    public final int pressureHpa;

    /** Visibility in metres. */
    public final int visibilityM;

    public WeatherData(String city, String country,
            double tempCelsius, double feelsLikeCelsius,
            int humidity, double windSpeedMs,
            String condition, String description,
            String iconCode, int pressureHpa, int visibilityM) {
        this.city = city;
        this.country = country;
        this.tempCelsius = tempCelsius;
        this.feelsLikeCelsius = feelsLikeCelsius;
        this.humidity = humidity;
        this.windSpeedMs = windSpeedMs;
        this.condition = condition;
        this.description = description;
        this.iconCode = iconCode;
        this.pressureHpa = pressureHpa;
        this.visibilityM = visibilityM;
    }
}