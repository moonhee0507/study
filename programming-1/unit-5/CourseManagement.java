import java.util.*;

public class CourseManagement {
    private static List<Course> courses = new ArrayList<>();
    private static Map<Student, Double> overallGrades = new HashMap<>();

    public static void addCourse(String code, String name, int capacity) {
        Course course = new Course(code, name, capacity);
        courses.add(course);
        System.out.println("Course added successfully.");
    }

    public static void enrollStudent(Student student, Course course) {
        if (course.enrollStudent()) {
            student.enrollStudent(course);
            System.out.println("Student enrolled successfully.");
        } else {
            System.out.println("Course has reached maximum capacity.");
        }
    }

    public static void assignGrade(Student student, Course course, double grade) {
        student.assignGrade(course, grade);
        System.out.println("Grade assigned successfully.");
    }

    public static void calculateOverallGrade(Student student) {
        Map<Course, Double> grades = student.getGrades();
        if (grades.isEmpty()) {
            System.out.println("No grades available.");
            return;
        }

        double total = 0;
        for (double grade : grades.values()) {
            total += grade;
        }
        double average = total / grades.size();
        overallGrades.put(student, average);

        System.out.println("Overall Grade: " + average);
    }

    public static List<Course> getCourses() {
        return courses;
    }
}
