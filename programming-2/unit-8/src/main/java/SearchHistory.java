import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages the list of recent weather searches.
 *
 * Each entry records the city name and the timestamp at which the
 * search was performed. Entries are stored in insertion order;
 * the most recent search appears last.
 *
 * The list is capped at MAX_ENTRIES to prevent unbounded growth
 * during a session. When the cap is reached, the oldest entry is
 * removed before the new one is added (FIFO eviction).
 *
 * @author Hee Moon
 */
public class SearchHistory {

    /** Maximum number of entries retained in memory. */
    private static final int MAX_ENTRIES = 10;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Immutable snapshot of a single search event.
     */
    public static class Entry {
        public final String city;
        public final String timestamp;

        private Entry(String city) {
            this.city = city;
            this.timestamp = LocalDateTime.now().format(FORMATTER);
        }

        @Override
        public String toString() {
            return timestamp + "  —  " + city;
        }
    }

    private final List<Entry> entries = new ArrayList<>();

    /**
     * Records a new search for the given city name.
     * Duplicate consecutive searches (same city) are still recorded
     * as separate entries with their own timestamps.
     *
     * @param city the city name that was searched
     */
    public void add(String city) {
        if (entries.size() >= MAX_ENTRIES) {
            entries.remove(0); // evict the oldest entry
        }
        entries.add(new Entry(city));
    }

    /**
     * Returns an unmodifiable view of all recorded entries,
     * oldest first.
     *
     * @return read-only list of search history entries
     */
    public List<Entry> getAll() {
        return Collections.unmodifiableList(entries);
    }

    /**
     * Removes all entries from the history.
     */
    public void clear() {
        entries.clear();
    }

    /**
     * Returns the number of entries currently stored.
     *
     * @return entry count
     */
    public int size() {
        return entries.size();
    }
}