import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;

import java.time.LocalTime;

/**
 * Provides a flat single-colour background based on the current time of day.
 *
 * The day is divided into four periods, each mapped to a distinct
 * grey/white tone so the app feels understated rather than decorative:
 * <ul>
 * <li>Dawn #F5E6C8 (warm amber-tinted)</li>
 * <li>Day #EAF4FB (light sky blue-tinted)</li>
 * <li>Sunset #F2DDD5 (muted terracotta)</li>
 * <li>Night #2E3440 (dark navy)</li>
 * </ul>
 *
 * @author Hee Moon
 */
public class BackgroundManager {

    /** The four time-of-day periods recognised by this class. */
    public enum Period {
        DAWN, DAY, SUNSET, NIGHT
    }

    /**
     * Returns the time-of-day period for the given hour.
     *
     * @param hour hour of day (0-23)
     * @return the corresponding Period
     */
    public static Period getPeriod(int hour) {
        if (hour >= 6 && hour <= 8)
            return Period.DAWN;
        if (hour >= 9 && hour <= 16)
            return Period.DAY;
        if (hour >= 17 && hour <= 19)
            return Period.SUNSET;
        return Period.NIGHT;
    }

    /**
     * Builds a flat Background colour for the current local time.
     *
     * @return Background appropriate for the current time of day
     */
    public static Background getBackground() {
        int hour = LocalTime.now().getHour();
        return getBackground(getPeriod(hour));
    }

    /**
     * Builds a flat Background colour for the given period.
     *
     * @param period time-of-day period
     * @return corresponding Background
     */
    public static Background getBackground(Period period) {
        Color bg;
        switch (period) {
            case DAWN:
                bg = Color.web("#F5E6C8");
                break; // warm amber-tinted
            case DAY:
                bg = Color.web("#EAF4FB");
                break; // light sky blue-tinted
            case SUNSET:
                bg = Color.web("#F2DDD5");
                break; // muted terracotta
            case NIGHT:
            default:
                bg = Color.web("#2E3440");
                break; // dark navy
        }
        return new Background(
                new BackgroundFill(bg, CornerRadii.EMPTY, Insets.EMPTY));
    }

    /**
     * Returns a short human-readable label for the current period.
     *
     * @return label such as "Day", "Sunset", etc.
     */
    public static String getPeriodLabel() {
        int hour = LocalTime.now().getHour();
        switch (getPeriod(hour)) {
            case DAWN:
                return "Dawn";
            case DAY:
                return "Day";
            case SUNSET:
                return "Sunset";
            case NIGHT:
                return "Night";
            default:
                return "";
        }
    }
}