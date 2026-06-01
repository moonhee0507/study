/**
 * Holds weather data for a single 3-hour forecast slot.
 *
 * OpenWeatherMap's free /forecast endpoint returns up to 40 slots
 * (5 days × 8 slots/day). WeatherPanel displays the first 5 slots
 * (next ~15 hours) as a short-term forecast strip.
 *
 * @author Hee Moon
 */
public class ForecastData {

    /** Display label for the time slot, e.g. "15:00". */
    public final String timeLabel;

    /** Temperature in Celsius for this slot. */
    public final double tempCelsius;

    /** Short condition label, e.g. "Clouds". */
    public final String condition;

    /** OpenWeatherMap icon code for this slot. */
    public final String iconCode;

    public ForecastData(String timeLabel, double tempCelsius,
            String condition, String iconCode) {
        this.timeLabel = timeLabel;
        this.tempCelsius = tempCelsius;
        this.condition = condition;
        this.iconCode = iconCode;
    }
}