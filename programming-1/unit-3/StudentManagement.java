import java.util.ArrayList;

/**
 * StudentManagement class handles operations related to students.
 * Static variables are used to maintain shared student records across the system.
 */
public class StudentManagement {

    // Shared student list for the entire system
    private static ArrayList<Student> studentList = new ArrayList<>();

    // Tracks total number of students
    private static int totalStudents = 0;

    /**
     * Adds a new student if the ID does not already exist.
     */
    public static void addStudent(String id, String name, int age, String grade) {

        if (findStudentById(id) != null) {
            System.out.println("Error: Student with this ID already exists.");
            return;
        }

        Student newStudent = new Student(id, name, age, grade);
        studentList.add(newStudent);
        totalStudents++;

        System.out.println("Student added successfully!");
    }

    /**
     * Updates student information based on ID.
     */
    public static void updateStudent(String id, String name, int age, String grade) {

        Student student = findStudentById(id);

        if (student == null) {
            System.out.println("Error: Student ID not found.");
            return;
        }

        student.setName(name);
        student.setAge(age);
        student.setGrade(grade);

        System.out.println("Student information updated successfully!");
    }

    /**
     * Displays student details.
     */
    public static void viewStudent(String id) {

        Student student = findStudentById(id);

        if (student == null) {
            System.out.println("Error: Student ID not found.");
        } else {
            System.out.println("\n--- Student Details ---");
            System.out.println(student);
        }
    }

    /**
     * Searches for a student by ID.
     */
    private static Student findStudentById(String id) {
        for (Student student : studentList) {
            if (student.getId().equals(id)) {
                return student;
            }
        }
        return null;
    }
}
