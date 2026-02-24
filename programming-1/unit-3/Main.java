import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== Student Record Management System =====");
            System.out.println("1. Add New Student");
            System.out.println("2. Update Student Information");
            System.out.println("3. View Student Details");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {

                    case 1:
                        addStudentInterface(scanner);
                        break;

                    case 2:
                        updateStudentInterface(scanner);
                        break;

                    case 3:
                        viewStudentInterface(scanner);
                        break;

                    case 4:
                        System.out.println("Exiting program. Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                choice = 0;
            }

        } while (choice != 4);

        scanner.close();
    }

    private static void addStudentInterface(Scanner scanner) {

        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        int age = readValidAge(scanner);

        System.out.print("Enter Grade: ");
        String grade = scanner.nextLine();

        StudentManagement.addStudent(id, name, age, grade);
    }

    private static void updateStudentInterface(Scanner scanner) {

        System.out.print("Enter Student ID to update: ");
        String id = scanner.nextLine();

        System.out.print("Enter New Name: ");
        String name = scanner.nextLine();

        int age = readValidAge(scanner);

        System.out.print("Enter New Grade: ");
        String grade = scanner.nextLine();

        StudentManagement.updateStudent(id, name, age, grade);
    }

    private static void viewStudentInterface(Scanner scanner) {

        System.out.print("Enter Student ID to view: ");
        String id = scanner.nextLine();

        StudentManagement.viewStudent(id);
    }

    private static int readValidAge(Scanner scanner) {

        int age = -1;

        while (age < 0) {
            try {
                System.out.print("Enter Age: ");
                age = Integer.parseInt(scanner.nextLine());

                if (age < 0) {
                    System.out.println("Age must be positive.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid age. Please enter a valid number.");
            }
        }

        return age;
    }
}
