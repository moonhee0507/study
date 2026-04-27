import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Stores and updates the current time and date.
 * It is used as a shared data object between the two threads.
 */
class ClockModel {

    /**
     * The date-time pattern never changes throughout the program's lifetime.
     * Using 'final' prevents accidental reassignment and makes the
     * intent (constant value) clear to any reader of the code.
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");

    /**
     * currentTime is declared volatile because it is written by BackgroundUpdater
     * and read by ClockDisplay concurrently.
     * Without 'volatile', the JVM may cache the value in a thread-local copy,
     * so ClockDisplay could read a stale timestamp.
     * 'volatile' guarantees that every thread always sees the most recently written
     * value from main memory.
     */
    private volatile String currentTime;

    /**
     * Captures the current system time and stores it in currentTime.
     * Called repeatedly by the BackgroundUpdater thread.
     */
    public void updateTime() {
        currentTime = LocalDateTime.now().format(FORMATTER);
    }

    /**
     * Returns the most recently updated time string.
     *
     * @return formatted date-time string, e.g. "14:05:30 27-04-2026"
     */
    public String getCurrentTime() {
        return currentTime;
    }
}

/**
 * BackgroundUpdater runs at LOW priority and continuously refreshes
 * the ClockModel's internal time every 500 ms.
 * Its sole responsibility is data preparation. It never writes to the console.
 */
class BackgroundUpdater extends Thread {

    /**
     * clock is declared 'final' because this thread is permanently bound
     * to one ClockModel instance at construction time and should never
     * be reassigned to a different object.
     */
    private final ClockModel clock;

    /**
     * running is declared 'volatile' so that the main thread can safely
     * signal this thread to stop by calling stopRunning().
     * Without 'volatile', the updated value of running might not be
     * visible to this thread immediately due to CPU caching.
     */
    private volatile boolean running;

    /**
     * Constructs a BackgroundUpdater linked to the given ClockModel.
     *
     * @param clock the shared ClockModel object to update
     */
    public BackgroundUpdater(ClockModel clock) {
        this.clock = clock;
        this.running = true;
        // Set LOW priority so the display thread is always preferred
        // by the scheduler for better timekeeping precision.
        this.setPriority(Thread.MIN_PRIORITY); // priority = 1
    }

    /**
     * Continuously updates the clock's time at 500 ms intervals
     * until running is set to false.
     */
    @Override
    public void run() {
        System.out.println("[BackgroundUpdater] started  | priority = "
                + getPriority());

        while (running) {
            clock.updateTime();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // Restore the interrupted flag and exit the loop cleanly.
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("[BackgroundUpdater] stopped.");
    }

    /**
     * Signals the run loop to exit on the next iteration.
     * Setting 'running = false' is safe because running is volatile.
     */
    public void stopRunning() {
        running = false;
    }
}

/**
 * ClockDisplay runs at HIGH priority and continuously prints the current
 * time held by the shared ClockModel to the console every 1000 ms.
 */
class ClockDisplay extends Thread {

    /**
     * clock is declared final for the same reason as in BackgroundUpdater:
     * this thread is permanently bound to one ClockModel and must not
     * be reassigned.
     */
    private final ClockModel clock;

    /**
     * running is declared volatile so that the main thread can safely
     * signal this thread to stop, just as in BackgroundUpdater.
     */
    private volatile boolean running;

    /**
     * Constructs a ClockDisplay linked to the given ClockModel.
     *
     * @param clock the shared ClockModel object to read from
     */
    public ClockDisplay(ClockModel clock) {
        this.clock = clock;
        this.running = true;
        // Set HIGH priority so this thread is preferred over
        // BackgroundUpdater, ensuring the display is always current.
        this.setPriority(Thread.MAX_PRIORITY); // priority = 10
    }

    /**
     * Continuously prints the clock's current time at 1000 ms intervals
     * until running is set to false.
     */
    @Override
    public void run() {
        System.out.println("[ClockDisplay]     started  | priority = "
                + getPriority());

        while (running) {
            String time = clock.getCurrentTime();
            if (time != null) {
                System.out.println("[ClockDisplay]     Current time: " + time);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("[ClockDisplay]     stopped.");
    }

    /**
     * Signals the run loop to exit on the next iteration.
     */
    public void stopRunning() {
        running = false;
    }
}

/**
 * Clock is the entry point of the Simple Clock Application.
 * It creates one shared ClockModel object, then starts two threads:
 * - BackgroundUpdater (priority 1) : keeps the time fresh
 * - ClockDisplay (priority 10): prints the time to the console
 * After 10 seconds both threads are stopped gracefully.
 *
 * @author Hee Moon
 */
public class Clock {

    public static void main(String[] args) {

        // 1. Create the shared ClockModel object
        ClockModel clock = new ClockModel();

        // 2. Create the two threads
        BackgroundUpdater updaterThread = new BackgroundUpdater(clock);
        ClockDisplay displayThread = new ClockDisplay(clock);

        // 3. Start the updater first so the clock has a value before
        // the display thread attempts to print it
        updaterThread.start();

        // Brief pause to let the updater capture the first timestamp
        // before the display thread starts reading it.
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // Preserve the interrupt status after sleep is interrupted.
            Thread.currentThread().interrupt();
        }

        displayThread.start();

        // 4. Let the clock run for 10 seconds, then stop gracefully
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        updaterThread.stopRunning();
        displayThread.stopRunning();

        // Interrupt sleeping threads so they exit immediately
        // rather than waiting for the current sleep to finish.
        updaterThread.interrupt();
        displayThread.interrupt();

        // Use join() to wait until both threads have fully terminated.
        try {
            updaterThread.join();
            displayThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n[Main] Clock application finished.");
    }
}