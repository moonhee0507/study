/**
 * Utility class for unit conversions used throughout the Weather App.
 *
 * All methods are static; this class is never instantiated.
 * Two temperature scales (Celsius / Fahrenheit) and two wind-speed
 * units (m/s / km/h) are supported, matching the assignment requirement
 * for switchable units.
 *
 * @author Hee Moon
 */
public final class UnitConverter {

    // Prevent instantiation.
    private UnitConverter() {
    }

    // -------------------------------------------------------------------------
    // Temperature
    // -------------------------------------------------------------------------

    /**
     * Converts Celsius to Fahrenheit.
     *
     * @param celsius temperature in degrees Celsius
     * @return equivalent temperature in degrees Fahrenheit
     */
    public static double celsiusToFahrenheit(double celsius) {
        return celsius * 9.0 / 5.0 + 32.0;
    }

    /**
     * Formats a Celsius value as a display string in the requested unit.
     *
     * @param celsius       temperature in Celsius
     * @param useFahrenheit true to display in Fahrenheit, false for Celsius
     * @return formatted string, e.g. "23.4 °C" or "74.1 °F"
     */
    public static String formatTemp(double celsius, boolean useFahrenheit) {
        if (useFahrenheit) {
            return String.format("%.1f °F", celsiusToFahrenheit(celsius));
        }
        return String.format("%.1f °C", celsius);
    }

    // -------------------------------------------------------------------------
    // Wind speed
    // -------------------------------------------------------------------------

    /**
     * Converts metres per second to kilometres per hour.
     *
     * @param ms wind speed in m/s
     * @return equivalent wind speed in km/h
     */
    public static double msToKmh(double ms) {
        return ms * 3.6;
    }

    /**
     * Formats a wind-speed value (given in m/s) as a display string.
     *
     * @param ms     wind speed in metres per second
     * @param useKmh true to display in km/h, false for m/s
     * @return formatted string, e.g. "5.2 m/s" or "18.7 km/h"
     */
    public static String formatWind(double ms, boolean useKmh) {
        if (useKmh) {
            return String.format("%.1f km/h", msToKmh(ms));
        }
        return String.format("%.1f m/s", ms);
    }

    /**
     * Converts metres to kilometres, formatted for visibility display.
     *
     * @param metres visibility in metres
     * @return formatted string, e.g. "10.0 km" or "800 m"
     */
    public static String formatVisibility(int metres) {
        if (metres >= 1000) {
            return String.format("%.1f km", metres / 1000.0);
        }
        return metres + " m";
    }
}