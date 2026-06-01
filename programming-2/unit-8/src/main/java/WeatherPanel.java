import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.List;

/**
 * Main UI panel of the Weather Information App.
 *
 * Builds the complete scene graph (search bar, current-weather display,
 * forecast strip, unit-toggle buttons, history panel) and wires all
 * user interactions to the appropriate back-end calls.
 *
 * Layout (top to bottom):
 * 
 * <pre>
 *   +----------------------------------+
 *   |  Search bar + Search button      |
 *   +----------------------------------+
 *   |  Condition | City/Country        |
 *   |            | Temp  FeelsLike     |
 *   |            | Description         |
 *   +----------------------------------+
 *   |  Humidity | Wind | Pressure      |
 *   +----------------------------------+
 *   |  [C / F]   [m/s / km/h]          |
 *   +----------------------------------+
 *   |  Forecast strip (5 slots)        |
 *   +----------------------------------+
 *   |  Search history list             |
 *   +----------------------------------+
 * </pre>
 *
 * @author Hee Moon
 */
public class WeatherPanel {

    // ---- Configuration -------------------------------------------------------
    private static final String API_KEY = "313c1286fe9937b52a03d8940e24ff4b";

    // ---- Colours (dark-on-light palette for flat grey background) ------------
    private static final String C_TEXT_PRIMARY = "#1A1A1A";
    private static final String C_TEXT_SECONDARY = "#555555";
    private static final String C_TEXT_MUTED = "#888888";
    private static final String C_CARD_BG = "#FFFFFF";
    private static final String C_CARD_BORDER = "#DDDDDD";
    private static final String C_BTN_INACTIVE = "#E0E0E0";
    private static final String C_ERROR = "#B00020";
    private static final String C_SEARCH_BTN = "#333333";

    // ---- Back-end services ---------------------------------------------------
    private final ApiClient apiClient = new ApiClient(API_KEY);
    private final SearchHistory searchHistory = new SearchHistory();

    // ---- Unit state ----------------------------------------------------------
    /** true = display Fahrenheit; false = display Celsius (default). */
    private boolean useFahrenheit = false;
    /** true = display km/h; false = display m/s (default). */
    private boolean useKmh = false;

    // ---- Cached data (needed for unit-toggle redraw) -------------------------
    private WeatherData lastWeather = null;
    private List<ForecastData> lastForecast = null;

    // ---- Root container ------------------------------------------------------
    private final VBox root = new VBox(12);

    // ---- UI controls updated after a search ----------------------------------
    private final Label conditionTextLabel = new Label("");
    private final Label cityLabel = new Label("");
    private final Label tempLabel = new Label("");
    private final Label feelsLikeLabel = new Label("");
    private final Label descLabel = new Label("");
    private final Label humidityLabel = new Label("Humidity: --");
    private final Label windLabel = new Label("Wind: --");
    private final Label pressureLabel = new Label("Pressure: --");
    private final Label visibilityLabel = new Label("Visibility: --");
    private final Label statusLabel = new Label("Enter a city name and press Search.");
    private final Label tempUnitLabel = new Label("Temp: ");
    private final Label windUnitLabel = new Label("Wind: ");
    private final HBox forecastBox = new HBox(8);
    private final VBox historyBox = new VBox(4);

    // Card containers — kept as fields so applyBackground() can restyle them.
    private HBox weatherCard = null;
    private HBox detailRow = null;
    private VBox forecastSection = null;
    private VBox historySection = null;
    private VBox testButtonSection = null;

    /**
     * When non-null, overrides the real time-of-day period for background
     * preview. Set by the test buttons; reset to null on each real search.
     */
    private BackgroundManager.Period forcedPeriod = null;

    // ---- Toggle buttons ------------------------------------------------------
    private final ToggleButton btnCelsius = new ToggleButton("C");
    private final ToggleButton btnFahr = new ToggleButton("F");
    private final ToggleButton btnMs = new ToggleButton("m/s");
    private final ToggleButton btnKmh = new ToggleButton("km/h");

    // -------------------------------------------------------------------------

    public WeatherPanel() {
        buildUI();
    }

    /** Returns the root node to be placed in the Scene. */
    public VBox getRoot() {
        return root;
    }

    // =========================================================================
    // UI construction
    // =========================================================================

    private void buildUI() {
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        // Build all cards first so their field references are populated,
        // then call applyBackground() to apply the correct Night/Day colours.
        root.getChildren().addAll(
                buildSearchBar(),
                buildWeatherCard(),
                buildDetailRow(),
                buildUnitToggleRow(),
                buildForecastSection(),
                buildHistorySection(),
                buildStatusBar(),
                buildTestButtons());

        applyBackground();
    }

    // ---- Search bar ----------------------------------------------------------

    private HBox buildSearchBar() {
        TextField searchField = new TextField();
        searchField.setPromptText("Enter city name...");
        searchField.setFont(Font.font(14));
        searchField.setPrefWidth(380);
        searchField.setStyle("-fx-background-color: white;"
                + "-fx-border-color: " + C_CARD_BORDER + ";"
                + "-fx-border-radius: 4; -fx-background-radius: 4;");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button searchBtn = new Button("Search");
        searchBtn.setFont(Font.font("System", FontWeight.BOLD, 14));
        searchBtn.setStyle("-fx-background-color: " + C_SEARCH_BTN + ";"
                + "-fx-text-fill: white; -fx-background-radius: 4;");

        searchBtn.setOnAction(e -> handleSearch(searchField.getText()));
        searchField.setOnAction(e -> handleSearch(searchField.getText()));

        HBox bar = new HBox(8, searchField, searchBtn);
        bar.setAlignment(Pos.CENTER);
        return bar;
    }

    // ---- Weather card --------------------------------------------------------

    private HBox buildWeatherCard() {
        // Result is stored in the weatherCard field for Night-mode restyling.
        // Condition text instead of emoji
        conditionTextLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        conditionTextLabel.setTextFill(Color.web(C_TEXT_SECONDARY));
        conditionTextLabel.setMinWidth(80);
        conditionTextLabel.setTextAlignment(TextAlignment.CENTER);

        cityLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        cityLabel.setTextFill(Color.web(C_TEXT_PRIMARY));

        tempLabel.setFont(Font.font("System", FontWeight.BOLD, 40));
        tempLabel.setTextFill(Color.web(C_TEXT_PRIMARY));

        feelsLikeLabel.setFont(Font.font(13));
        feelsLikeLabel.setTextFill(Color.web(C_TEXT_SECONDARY));

        descLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        descLabel.setTextFill(Color.web(C_TEXT_SECONDARY));

        VBox textBox = new VBox(4, cityLabel, tempLabel, feelsLikeLabel, descLabel);
        textBox.setAlignment(Pos.CENTER_LEFT);

        HBox card = new HBox(16, conditionTextLabel, textBox);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(14, 16, 14, 16));
        card.setStyle("-fx-background-color: " + C_CARD_BG + ";"
                + "-fx-border-color: " + C_CARD_BORDER + ";"
                + "-fx-border-radius: 6; -fx-background-radius: 6;");
        weatherCard = card;
        return card;
    }

    // ---- Detail row ----------------------------------------------------------

    private HBox buildDetailRow() {
        styleDetailLabel(humidityLabel);
        styleDetailLabel(windLabel);
        styleDetailLabel(pressureLabel);
        styleDetailLabel(visibilityLabel);

        HBox row = new HBox(12,
                wrapDetail("Humidity", humidityLabel),
                wrapDetail("Wind", windLabel),
                wrapDetail("Pressure", pressureLabel),
                wrapDetail("Visibility", visibilityLabel));
        row.setAlignment(Pos.CENTER);
        detailRow = row;
        return row;
    }

    private void styleDetailLabel(Label l) {
        l.setFont(Font.font(13));
        l.setTextFill(Color.web(C_TEXT_PRIMARY));
    }

    private VBox wrapDetail(String heading, Label l) {
        Label headingLbl = new Label(heading);
        headingLbl.setFont(Font.font("System", FontWeight.BOLD, 11));
        headingLbl.setTextFill(Color.web(C_TEXT_MUTED));

        VBox box = new VBox(3, headingLbl, l);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(8));
        box.setStyle("-fx-background-color: " + C_CARD_BG + ";"
                + "-fx-border-color: " + C_CARD_BORDER + ";"
                + "-fx-border-radius: 6; -fx-background-radius: 6;");
        box.setMinWidth(120);
        return box;
    }

    // ---- Unit toggle row -----------------------------------------------------

    private HBox buildUnitToggleRow() {
        btnCelsius.setSelected(true);
        styleToggle(btnCelsius, true);
        styleToggle(btnFahr, false);

        btnCelsius.setOnAction(e -> {
            useFahrenheit = false;
            btnCelsius.setSelected(true);
            btnFahr.setSelected(false);
            styleToggle(btnCelsius, true);
            styleToggle(btnFahr, false);
            refreshUnits();
        });
        btnFahr.setOnAction(e -> {
            useFahrenheit = true;
            btnFahr.setSelected(true);
            btnCelsius.setSelected(false);
            styleToggle(btnFahr, true);
            styleToggle(btnCelsius, false);
            refreshUnits();
        });

        btnMs.setSelected(true);
        styleToggle(btnMs, true);
        styleToggle(btnKmh, false);

        btnMs.setOnAction(e -> {
            useKmh = false;
            btnMs.setSelected(true);
            btnKmh.setSelected(false);
            styleToggle(btnMs, true);
            styleToggle(btnKmh, false);
            refreshUnits();
        });
        btnKmh.setOnAction(e -> {
            useKmh = true;
            btnKmh.setSelected(true);
            btnMs.setSelected(false);
            styleToggle(btnKmh, true);
            styleToggle(btnMs, false);
            refreshUnits();
        });

        HBox tempGroup = new HBox(0, btnCelsius, btnFahr);
        HBox windGroup = new HBox(0, btnMs, btnKmh);

        String toggleTextColor = isNightMode() ? "#ECEFF4" : C_TEXT_PRIMARY;

        tempUnitLabel.setTextFill(Color.web(toggleTextColor));
        tempUnitLabel.setFont(Font.font(13));

        windUnitLabel.setTextFill(Color.web(toggleTextColor));
        windUnitLabel.setFont(Font.font(13));

        HBox row = new HBox(16, tempUnitLabel, tempGroup, windUnitLabel, windGroup);
        row.setAlignment(Pos.CENTER);
        return row;
    }

    private void styleToggle(ToggleButton btn, boolean active) {
        boolean isNight = isNightMode();

        if (active) {
            // Active: white background with dark bold text — clearly "selected"
            String activeBg = isNight ? "#ECEFF4" : "#FFFFFF";
            String activeText = isNight ? "#2E3440" : "#1A1A1A";
            btn.setStyle("-fx-background-color: " + activeBg + ";"
                    + "-fx-text-fill: " + activeText + "; -fx-font-weight: bold;"
                    + "-fx-border-color: #888888; -fx-border-radius: 4;"
                    + "-fx-background-radius: 4;");
        } else {
            // Inactive: blends into background — clearly "unselected"
            String inactiveBg = isNight ? "#3B4252" : "#E0E0E0";
            String inactiveText = isNight ? "#6B7280" : "#AAAAAA";
            btn.setStyle("-fx-background-color: " + inactiveBg + ";"
                    + "-fx-text-fill: " + inactiveText + ";"
                    + "-fx-border-color: transparent;"
                    + "-fx-background-radius: 4;");
        }
    }

    // ---- Forecast section ----------------------------------------------------

    private VBox buildForecastSection() {
        Label title = new Label("Short-term Forecast");
        title.setFont(Font.font("System", FontWeight.BOLD, 13));
        title.setTextFill(Color.web(isNightMode() ? "#A0A8B8" : C_TEXT_MUTED));

        forecastBox.setAlignment(Pos.CENTER);
        forecastBox.setPadding(new Insets(6, 0, 2, 0));

        VBox section = new VBox(6, title, forecastBox);
        section.setPadding(new Insets(12));
        section.setStyle("-fx-background-color: " + C_CARD_BG + ";"
                + "-fx-border-color: " + C_CARD_BORDER + ";"
                + "-fx-border-radius: 6; -fx-background-radius: 6;");
        forecastSection = section;
        return section;
    }

    // ---- History section -----------------------------------------------------

    private VBox buildHistorySection() {
        boolean isNight = isNightMode();
        String historyTitleColor = isNight ? "#A0A8B8" : C_TEXT_MUTED;
        String clearBtnBg = isNight ? "#4C566A" : C_BTN_INACTIVE;
        String clearBtnText = isNight ? "#ECEFF4" : C_TEXT_PRIMARY;

        Label title = new Label("Search History");
        title.setFont(Font.font("System", FontWeight.BOLD, 13));
        title.setTextFill(Color.web(historyTitleColor));

        Button clearBtn = new Button("Clear");
        clearBtn.setFont(Font.font(12));
        clearBtn.setStyle("-fx-background-color: " + clearBtnBg + ";"
                + "-fx-text-fill: " + clearBtnText + ";"
                + "-fx-background-radius: 4;");
        clearBtn.setOnAction(e -> {
            searchHistory.clear();
            refreshHistory();
        });

        HBox header = new HBox(8, title, clearBtn);
        header.setAlignment(Pos.CENTER_LEFT);

        historyBox.setPadding(new Insets(4, 0, 0, 0));

        VBox section = new VBox(6, header, historyBox);
        section.setPadding(new Insets(12));
        section.setStyle("-fx-background-color: " + C_CARD_BG + ";"
                + "-fx-border-color: " + C_CARD_BORDER + ";"
                + "-fx-border-radius: 6; -fx-background-radius: 6;");
        historySection = section;
        return section;
    }

    // ---- Status bar ----------------------------------------------------------

    /**
     * Builds a status bar that displays search results, errors, and
     * last-updated timestamps.
     *
     * @return HBox containing the status label
     */
    private HBox buildStatusBar() {
        statusLabel.setFont(Font.font(12));
        statusLabel.setWrapText(true);

        HBox bar = new HBox(statusLabel);
        bar.setPadding(new Insets(4, 2, 0, 2));
        return bar;
    }

    // ---- Test buttons --------------------------------------------------------

    /**
     * Builds a row of four buttons for testing dynamic background periods.
     * Each button forces a specific Period so the grader can verify all
     * four background colours without waiting for the actual time of day.
     *
     * These buttons are for demonstration purposes only.
     *
     * @return VBox containing the test button strip
     */
    private VBox buildTestButtons() {
        Label heading = new Label("Background Preview");
        heading.setFont(Font.font("System", FontWeight.BOLD, 11));
        heading.setTextFill(Color.web(C_TEXT_MUTED));

        Button btnDawn = makeTestBtn("Dawn (06-08)");
        Button btnDay = makeTestBtn("Day (09-16)");
        Button btnSunset = makeTestBtn("Sunset (17-19)");
        Button btnNight = makeTestBtn("Night (20-05)");

        btnDawn.setOnAction(e -> forceBackground(BackgroundManager.Period.DAWN));
        btnDay.setOnAction(e -> forceBackground(BackgroundManager.Period.DAY));
        btnSunset.setOnAction(e -> forceBackground(BackgroundManager.Period.SUNSET));
        btnNight.setOnAction(e -> forceBackground(BackgroundManager.Period.NIGHT));

        HBox btnRow = new HBox(6, btnDawn, btnDay, btnSunset, btnNight);
        btnRow.setAlignment(Pos.CENTER);

        VBox section = new VBox(6, heading, btnRow);
        section.setPadding(new Insets(10));
        section.setAlignment(Pos.CENTER);
        section.setStyle("-fx-background-color: transparent;");
        testButtonSection = section;
        return section;
    }

    private Button makeTestBtn(String label) {
        Button btn = new Button(label);
        btn.setFont(Font.font(11));
        btn.setStyle("-fx-background-color: " + C_BTN_INACTIVE + ";"
                + "-fx-text-fill: " + C_TEXT_SECONDARY + ";"
                + "-fx-background-radius: 4;");
        return btn;
    }

    /** Temporarily forces a background period for preview purposes. */
    private void forceBackground(BackgroundManager.Period period) {
        forcedPeriod = period;
        applyForcedBackground();
    }

    // =========================================================================
    // Event handlers
    // =========================================================================

    /**
     * Performs a weather search for the given city name.
     * Network calls run on a background thread (Task) to keep the UI responsive.
     * Results are applied back on the JavaFX Application Thread via
     * Platform.runLater.
     *
     * @param city raw city name from the search field
     */
    private void handleSearch(String city) {
        if (city == null || city.trim().isEmpty()) {
            showStatus("Please enter a city name.", true);
            return;
        }

        showStatus("Searching for \"" + city.trim() + "\"...", false);

        Task<Void> task = new Task<>() {
            WeatherData weather;
            List<ForecastData> forecast;

            @Override
            protected Void call() throws Exception {
                weather = apiClient.fetchCurrentWeather(city);
                forecast = apiClient.fetchForecast(city);
                return null;
            }

            @Override
            protected void succeeded() {
                lastWeather = weather;
                lastForecast = forecast;

                forcedPeriod = null; // restore real time-of-day background
                searchHistory.add(weather.city + ", " + weather.country);
                applyBackground();
                displayWeather();
                displayForecast();
                refreshHistory();
                showStatus("Last updated: " + java.time.LocalDateTime.now()
                        .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"))
                        + "  |  " + BackgroundManager.getPeriodLabel(), false);
            }

            @Override
            protected void failed() {
                Throwable ex = getException();
                showStatus("Error: " + ex.getMessage(), true);
            }
        };

        new Thread(task).start();
    }

    // =========================================================================
    // Display methods
    // =========================================================================

    /** Renders the current weather using the cached lastWeather data. */
    private void displayWeather() {
        if (lastWeather == null)
            return;
        WeatherData w = lastWeather;

        conditionTextLabel.setText(conditionText(w.iconCode));
        cityLabel.setText(w.city + ", " + w.country);
        tempLabel.setText(UnitConverter.formatTemp(w.tempCelsius, useFahrenheit));
        feelsLikeLabel.setText("Feels like "
                + UnitConverter.formatTemp(w.feelsLikeCelsius, useFahrenheit));
        descLabel.setText(capitalize(w.description));

        humidityLabel.setText(w.humidity + " %");
        windLabel.setText(UnitConverter.formatWind(w.windSpeedMs, useKmh));
        pressureLabel.setText(w.pressureHpa + " hPa");
        visibilityLabel.setText(UnitConverter.formatVisibility(w.visibilityM));
    }

    /** Renders the forecast strip using the cached lastForecast data. */
    private void displayForecast() {
        forecastBox.getChildren().clear();
        if (lastForecast == null)
            return;

        for (ForecastData slot : lastForecast) {
            Label timeL = new Label(slot.timeLabel);
            timeL.setFont(Font.font("System", FontWeight.BOLD, 12));
            timeL.setTextFill(Color.web(C_TEXT_MUTED));

            Label condL = new Label(conditionText(slot.iconCode));
            condL.setFont(Font.font("System", FontWeight.BOLD, 12));
            condL.setTextFill(Color.web(C_TEXT_SECONDARY));

            Label tempL = new Label(
                    UnitConverter.formatTemp(slot.tempCelsius, useFahrenheit));
            tempL.setFont(Font.font(12));
            tempL.setTextFill(Color.web(C_TEXT_PRIMARY));

            boolean isNight = isNightMode();
            String fCardBg = isNight ? "#3B4252" : "#F7F7F7";
            String fCardBorder = isNight ? "#4C566A" : C_CARD_BORDER;
            String fTextPri = isNight ? "#ECEFF4" : C_TEXT_PRIMARY;
            String fTextSec = isNight ? "#D8DEE9" : C_TEXT_SECONDARY;
            String fTextMuted = isNight ? "#A0A8B8" : C_TEXT_MUTED;

            timeL.setTextFill(Color.web(fTextMuted));
            condL.setTextFill(Color.web(fTextSec));
            tempL.setTextFill(Color.web(fTextPri));

            VBox card = new VBox(4, timeL, condL, tempL);
            card.setAlignment(Pos.CENTER);
            card.setPadding(new Insets(8, 12, 8, 12));
            card.setStyle("-fx-background-color: " + fCardBg + ";"
                    + "-fx-border-color: " + fCardBorder + ";"
                    + "-fx-border-radius: 4; -fx-background-radius: 4;");
            forecastBox.getChildren().add(card);
        }
    }

    /** Rebuilds the history list from SearchHistory. */
    private void refreshHistory() {
        historyBox.getChildren().clear();
        List<SearchHistory.Entry> entries = searchHistory.getAll();
        if (entries.isEmpty()) {
            Label none = new Label("No searches yet.");
            none.setTextFill(Color.web(isNightMode() ? "#A0A8B8" : C_TEXT_MUTED));
            none.setFont(Font.font(12));
            historyBox.getChildren().add(none);
            return;
        }
        String historyTextColor = isNightMode() ? "#D8DEE9" : C_TEXT_SECONDARY;

        for (int i = entries.size() - 1; i >= 0; i--) {
            Label l = new Label(entries.get(i).toString());
            l.setFont(Font.font(12));
            l.setTextFill(Color.web(historyTextColor));
            historyBox.getChildren().add(l);
        }
    }

    /**
     * Re-renders temperature and wind labels after a unit toggle.
     * No new network request is made.
     */
    private void refreshUnits() {
        displayWeather();
        displayForecast();
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    /** Updates the status label. Error messages are shown in red. */
    private void showStatus(String msg, boolean isError) {
        Platform.runLater(() -> {
            statusLabel.setText(msg);
            statusLabel.setTextFill(
                    isError ? Color.web(C_ERROR) : Color.web(C_TEXT_MUTED));
        });
    }

    // =========================================================================
    // Period helpers
    // =========================================================================

    /**
     * Returns the currently active Period, respecting forcedPeriod.
     * All colour-branching logic in this class uses this single method
     * so that the test-button override is honoured everywhere.
     *
     * @return active Period (forced or real time-of-day)
     */
    private BackgroundManager.Period currentPeriod() {
        return (forcedPeriod != null)
                ? forcedPeriod
                : BackgroundManager.getPeriod(java.time.LocalTime.now().getHour());
    }

    /**
     * Convenience wrapper: returns true when the active period is Night.
     *
     * @return true if Night period is active
     */
    private boolean isNightMode() {
        return currentPeriod() == BackgroundManager.Period.NIGHT;
    }

    /**
     * Applies the time-of-day background to the root pane and adjusts
     * card backgrounds and text colours for legibility.
     *
     * If forcedPeriod is set (via test buttons), uses that period instead
     * of the real current time. Night (#2E3440) uses dark card surfaces
     * and light text. All other periods use white cards and dark text.
     */
    private void applyBackground() {
        BackgroundManager.Period period = currentPeriod();
        root.setBackground(BackgroundManager.getBackground(period));

        boolean isNight = isNightMode();

        String cardBg = isNight ? "#3B4252" : C_CARD_BG;
        String cardBorder = isNight ? "#4C566A" : C_CARD_BORDER;
        String textPri = isNight ? "#ECEFF4" : C_TEXT_PRIMARY;
        String textSec = isNight ? "#D8DEE9" : C_TEXT_SECONDARY;
        String textMuted = isNight ? "#A0A8B8" : C_TEXT_MUTED;

        String cardStyle = "-fx-background-color: " + cardBg + ";"
                + "-fx-border-color: " + cardBorder + ";"
                + "-fx-border-radius: 6; -fx-background-radius: 6;";

        // Repaint card containers
        if (weatherCard != null)
            weatherCard.setStyle(cardStyle);
        if (forecastSection != null)
            forecastSection.setStyle(cardStyle);
        if (historySection != null)
            historySection.setStyle(cardStyle);

        // Repaint detail boxes inside detailRow
        if (detailRow != null) {
            detailRow.getChildren().forEach(node -> {
                if (node instanceof VBox) {
                    String detailStyle = "-fx-background-color: " + cardBg + ";"
                            + "-fx-border-color: " + cardBorder + ";"
                            + "-fx-border-radius: 6; -fx-background-radius: 6;";
                    ((VBox) node).setStyle(detailStyle);
                }
            });
        }

        // Update text labels
        cityLabel.setTextFill(Color.web(textPri));
        tempLabel.setTextFill(Color.web(textPri));
        feelsLikeLabel.setTextFill(Color.web(textSec));
        descLabel.setTextFill(Color.web(textSec));
        conditionTextLabel.setTextFill(Color.web(textSec));
        humidityLabel.setTextFill(Color.web(textPri));
        windLabel.setTextFill(Color.web(textPri));
        pressureLabel.setTextFill(Color.web(textPri));
        visibilityLabel.setTextFill(Color.web(textPri));
        statusLabel.setTextFill(Color.web(textMuted));
        tempUnitLabel.setTextFill(Color.web(textPri));
        windUnitLabel.setTextFill(Color.web(textPri));

        // Update test button strip styling
        if (testButtonSection != null) {
            testButtonSection.getChildren().forEach(node -> {
                if (node instanceof HBox) {
                    ((HBox) node).getChildren().forEach(btn -> {
                        if (btn instanceof Button) {
                            String tbBg = isNight ? "#4C566A" : C_BTN_INACTIVE;
                            String tbText = isNight ? "#D8DEE9" : C_TEXT_SECONDARY;
                            ((Button) btn).setStyle(
                                    "-fx-background-color: " + tbBg + ";"
                                            + "-fx-text-fill: " + tbText + ";"
                                            + "-fx-background-radius: 4;");
                        }
                        if (btn instanceof Label) {
                            ((Label) btn).setTextFill(Color.web(textMuted));
                        }
                    });
                }
                if (node instanceof Label) {
                    ((Label) node).setTextFill(Color.web(textMuted));
                }
            });
        }

        // Refresh forecast cards and history labels to pick up new colours
        displayForecast();
        refreshHistory();
    }

    /**
     * Re-applies the background for the forced period.
     * Called exclusively by the test buttons.
     */
    private void applyForcedBackground() {
        applyBackground();
    }

    /**
     * Maps an OpenWeatherMap icon code to a short text label.
     * Replaces emoji with plain text so the UI looks understated.
     *
     * Icon code format: two digits + "d" (day) or "n" (night).
     *
     * @param code icon code from the API, e.g. "01d", "10n"
     * @return short condition label, e.g. "Clear", "Rain"
     */
    private String conditionText(String code) {
        if (code == null || code.length() < 2)
            return "N/A";
        String prefix = code.substring(0, 2);
        switch (prefix) {
            case "01":
                return "Clear";
            case "02":
                return "Partly Cloudy";
            case "03":
                return "Cloudy";
            case "04":
                return "Overcast";
            case "09":
                return "Shower";
            case "10":
                return "Rain";
            case "11":
                return "Thunderstorm";
            case "13":
                return "Snow";
            case "50":
                return "Fog";
            default:
                return "Unknown";
        }
    }

    /** Capitalises the first character of a string. */
    private String capitalize(String s) {
        if (s == null || s.isEmpty())
            return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}