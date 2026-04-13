import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * A text analysis tool that performs character and word analysis
 * on user-provided input.
 *
 * @author Hee Moon
 */
public class TextAnalysisTool {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String text = getTextInput(scanner);

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    displayCharacterCount(text);
                    break;
                case "2":
                    displayWordCount(text);
                    break;
                case "3":
                    displayMostCommonCharacter(text);
                    break;
                case "4":
                    displayCharacterFrequency(text, scanner);
                    break;
                case "5":
                    displayWordFrequency(text, scanner);
                    break;
                case "6":
                    displayUniqueWordCount(text);
                    break;
                case "7":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }

        scanner.close();
        System.out.println("--- Done ---");
    }

    /**
     * Prints the main menu.
     */
    private static void printMenu() {
        System.out.println("--- Menu ---");
        System.out.println("1. Character count");
        System.out.println("2. Word count");
        System.out.println("3. Most common character");
        System.out.println("4. Character frequency");
        System.out.println("5. Word frequency");
        System.out.println("6. Unique word count");
        System.out.println("7. Exit");
        System.out.print("Choose an option: ");
    }

    /**
     * Prompts the user to enter a non-empty text input.
     *
     * @param scanner Scanner object for reading input
     * @return the text entered by the user
     */
    private static String getTextInput(Scanner scanner) {
        String text = "";
        while (text.trim().isEmpty()) {
            System.out.print("Enter a paragraph or lengthy text: ");
            text = scanner.nextLine();
            if (text.trim().isEmpty()) {
                System.out.println("Text cannot be empty. Please try again.\n");
            }
        }
        return text;
    }

    /**
     * Displays the total number of characters in the text.
     *
     * @param text the input text
     */
    private static void displayCharacterCount(String text) {
        System.out.println("Character count: " + text.length());
    }

    /**
     * Displays the total number of words in the text.
     * Words are assumed to be separated by spaces.
     *
     * @param text the input text
     */
    private static void displayWordCount(String text) {
        String[] words = text.trim().split("\\s+");
        System.out.println("Word count: " + words.length);
    }

    /**
     * Finds and displays the most frequently occurring character in the text.
     * Spaces are excluded. Case-insensitive.
     * If there is a tie, one of the tied characters is selected.
     *
     * @param text the input text
     */
    private static void displayMostCommonCharacter(String text) {
        Map<Character, Integer> freqMap = new HashMap<>();

        for (char c : text.toLowerCase().toCharArray()) {
            if (c != ' ') {
                freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
            }
        }

        char mostCommon = ' ';
        int maxCount = 0;
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostCommon = entry.getKey();
            }
        }

        System.out.println("Most common character: '" + mostCommon
                + "' (" + maxCount + " times)");
    }

    /**
     * Prompts the user to enter a single character and displays
     * how many times it appears in the text. Case-insensitive.
     *
     * @param text    the input text
     * @param scanner Scanner object for reading input
     */
    private static void displayCharacterFrequency(String text, Scanner scanner) {
        String input = "";
        while (input.length() != 1) {
            System.out.print("Enter a character to search: ");
            input = scanner.nextLine();
            if (input.length() != 1) {
                System.out.println("Please enter exactly one character.");
            }
        }

        char target = Character.toLowerCase(input.charAt(0));
        int count = 0;
        for (char c : text.toLowerCase().toCharArray()) {
            if (c == target)
                count++;
        }

        System.out.println("'" + input + "' appears " + count + " time(s).");
    }

    /**
     * Prompts the user to enter a word and displays how many times
     * it appears in the text. Case-insensitive.
     *
     * @param text    the input text
     * @param scanner Scanner object for reading input
     */
    private static void displayWordFrequency(String text, Scanner scanner) {
        String input = "";
        while (input.trim().isEmpty()) {
            System.out.print("Enter a word to search: ");
            input = scanner.nextLine();
            if (input.trim().isEmpty()) {
                System.out.println("Word cannot be empty. Please try again.");
            }
        }

        String target = input.trim().toLowerCase();
        String[] words = text.trim().toLowerCase().split("\\s+");
        int count = 0;
        for (String word : words) {
            if (word.equals(target))
                count++;
        }

        System.out.println("\"" + input.trim() + "\" appears " + count + " time(s).");
    }

    /**
     * Displays the number of unique words in the text.
     * Case-insensitive.
     *
     * @param text the input text
     */
    private static void displayUniqueWordCount(String text) {
        String[] words = text.trim().toLowerCase().split("\\s+");
        Set<String> uniqueWords = new HashSet<>();
        for (String word : words) {
            uniqueWords.add(word);
        }
        System.out.println("Unique word count: " + uniqueWords.size());
    }
}
