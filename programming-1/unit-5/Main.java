import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Default student (for demonstration purposes)
        Student student = new Student("Default Student", "S001");

        while (true) {
            System.out.println("\n===== Course Management System =====");
            System.out.println("1. Add Course");
            System.out.println("2. Enroll Student");
            System.out.println("3. Assign Grade");
            System.out.println("4. Calculate Overall Grade");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {

                case 1:
                    System.out.print("Course Code: ");
                    String code = scanner.nextLine();

                    System.out.print("Course Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Capacity: ");
                    int capacity = scanner.nextInt();

                    CourseManagement.addCourse(code, name, capacity);
                    break;

                case 2:
                    if (CourseManagement.getCourses().isEmpty()) {
                        System.out.println("No courses available.");
                        break;
                    }

                    Course course = CourseManagement.getCourses().get(0);
                    CourseManagement.enrollStudent(student, course);
                    break;

                case 3:
                    if (CourseManagement.getCourses().isEmpty()) {
                        System.out.println("No courses available.");
                        break;
                    }

                    Course courseForGrade = CourseManagement.getCourses().get(0);

                    System.out.print("Enter Grade: ");
                    double grade = scanner.nextDouble();

                    CourseManagement.assignGrade(student, courseForGrade, grade);
                    break;

                case 4:
                    CourseManagement.calculateOverallGrade(student);
                    break;

                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}