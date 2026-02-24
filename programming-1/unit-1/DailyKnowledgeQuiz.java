import java.util.Scanner;

/**
 * Programming Assignment Unit 1
 * Daily Knowledge Quiz
 *
 * This program presents five multiple-choice general knowledge questions.
 * The user selects answers labeled A, B, C, or D.
 * The program validates input, checks answers using switch-case,
 * and displays the final score as a percentage.
 */
public class DailyKnowledgeQuiz {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int score = 0;
        String answer;

        // Question 1
        System.out.println("Question 1: What is the capital city of Canada?");
        System.out.println("A. Toronto");
        System.out.println("B. Vancouver");
        System.out.println("C. Ottawa");
        System.out.println("D. Montreal");

        answer = getValidAnswer(scanner);
        score += checkAnswer(answer, "C");

        // Question 2
        System.out.println("\nQuestion 2: Which planet is known as the Red Planet?");
        System.out.println("A. Venus");
        System.out.println("B. Mars");
        System.out.println("C. Jupiter");
        System.out.println("D. Saturn");

        answer = getValidAnswer(scanner);
        score += checkAnswer(answer, "B");

        // Question 3
        System.out.println("\nQuestion 3: How many days are there in a leap year?");
        System.out.println("A. 364");
        System.out.println("B. 365");
        System.out.println("C. 366");
        System.out.println("D. 367");

        answer = getValidAnswer(scanner);
        score += checkAnswer(answer, "C");

        // Question 4
        System.out.println("\nQuestion 4: Which ocean is the largest on Earth?");
        System.out.println("A. Atlantic Ocean");
        System.out.println("B. Indian Ocean");
        System.out.println("C. Arctic Ocean");
        System.out.println("D. Pacific Ocean");

        answer = getValidAnswer(scanner);
        score += checkAnswer(answer, "D");

        // Question 5
        System.out.println("\nQuestion 5: What gas do plants primarily use for photosynthesis?");
        System.out.println("A. Oxygen");
        System.out.println("B. Nitrogen");
        System.out.println("C. Carbon Dioxide");
        System.out.println("D. Hydrogen");

        answer = getValidAnswer(scanner);
        score += checkAnswer(answer, "C");

        // Final score calculation
        double percentage = (score / 5.0) * 100;

        System.out.println("\nQuiz Completed");
        System.out.println("Correct answers: " + score + " out of 5");
        System.out.println("Final Score: " + percentage + "%");

        scanner.close();
    }

    /**
     * Validates user input to ensure it is A, B, C, or D.
     */
    public static String getValidAnswer(Scanner scanner) {
        String input;

        while (true) {
            System.out.print("Enter your answer (A, B, C, or D): ");
            input = scanner.nextLine().toUpperCase();

            if (input.equals("A") || input.equals("B")
                    || input.equals("C") || input.equals("D")) {
                return input;
            } else {
                System.out.println("Invalid input. Please enter A, B, C, or D.");
            }
        }
    }

    /**
     * Checks the answer using a switch-case statement.
     * Returns 1 if the answer is correct, otherwise 0.
     */
    public static int checkAnswer(String userAnswer, String correctAnswer) {

        switch (userAnswer) {
            case "A":
            case "B":
            case "C":
            case "D":
                if (userAnswer.equals(correctAnswer)) {
                    return 1;
                }
                break;
        }
        return 0;
    }
}