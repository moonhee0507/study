# Weather Information App

## Dependencies

| Item               | Version                                      |
| ------------------ | -------------------------------------------- |
| JDK                | 17.0.15                                      |
| JavaFX SDK         | 21.0.11                                      |
| Maven              | 3.x+                                         |
| OpenWeatherMap API | Free plan (Current Weather + 5-day Forecast) |

JavaFX is the LTS version 21.0.11, compatible with JDK 17. Maven automatically downloads the JavaFX dependency via `pom.xml` — no separate SDK installation is required.

---

## File Structure

```
unit-8/
├── README.md
├── pom.xml                          — Maven build config; declares JavaFX 21 dependency
│                                      and javafx-maven-plugin for mvn javafx:run
└── src/
    └── main/
        └── java/
            ├── WeatherApp.java          — Entry point; launches the JavaFX Stage
            ├── WeatherPanel.java        — Full UI layout and event handling
            ├── ApiClient.java           — OpenWeatherMap API calls and JSON parsing
            ├── WeatherData.java         — Data class for current weather
            ├── ForecastData.java        — Data class for one forecast slot
            ├── SearchHistory.java       — Search history with timestamps
            ├── UnitConverter.java       — Temperature and wind speed conversion
            └── BackgroundManager.java   — Time-of-day background colour logic
```

---

## How to Run

### Step 1: Install Prerequisites

Ensure the following are installed:

```bash
java -version      # should show 17.x
mvn --version      # should show 3.x
```

If Maven is not installed (macOS):

```bash
brew install maven
```

### Step 2: Compile

```bash
cd unit-8
mvn clean compile
```

Expected output:

```
[INFO] BUILD SUCCESS
```

### Step 3: Run

```bash
mvn javafx:run
```

The Weather Information App window will open. Enter a city name (e.g. `Seoul`, `London`, `New York`) and press **Search** or **Enter**.

---

## Implementation Details

| Design Point             | Explanation                                                                                                                                                                                                                                              |
| ------------------------ | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **API Integration**      | `ApiClient` uses `HttpURLConnection` (Java standard library) to call two OpenWeatherMap endpoints: `/weather` for current conditions and `/forecast` for 5-slot short-term forecast. No external HTTP library is required.                               |
| **JSON Parsing**         | JSON is parsed manually using `String` operations in `ApiClient`. The `extract()` helper locates a key-value pair, and `extractFromArray()` reads the first element of a JSON array field (e.g. `weather[0].main`). No third-party JSON library is used. |
| **GUI Framework**        | JavaFX with a flat layout. All components are built programmatically — no FXML. `WeatherPanel` constructs the full scene graph and wires all event handlers in one class.                                                                                |
| **Background Threading** | Search requests run on a JavaFX `Task` background thread to prevent UI freezing. Results are applied back on the JavaFX Application Thread via `Platform.runLater()`.                                                                                    |
| **Unit Conversion**      | `UnitConverter` provides static methods for Celsius↔Fahrenheit and m/s↔km/h. Conversion is applied at render time from the raw Celsius/m/s values stored in `WeatherData`, so toggling units requires no new API call.                                   |
| **Search History**       | `SearchHistory` stores up to 10 entries (city name + timestamp) in an `ArrayList`. When the cap is reached, the oldest entry is evicted (FIFO). Entries are displayed newest-first in the UI.                                                            |
| **Dynamic Background**   | `BackgroundManager` maps the current hour to one of four periods (Dawn, Day, Sunset, Night), each with a distinct flat grey/white colour. The background updates on every successful search.                                                             |
| **Error Handling**       | HTTP error responses (e.g. 404 for unknown city) are read from the error stream and surfaced to the user via the status label. Network timeouts are set to 8 seconds.                                                                                    |
