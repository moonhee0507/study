import java.util.*;

public class Student {
    private String name;
    private String studentId;
    private List<Course> enrolledCourses;
    private Map<Course, Double> grades;

    public Student(String name, String studentId) {
        this.name = name;
        this.studentId = studentId;
        this.enrolledCourses = new ArrayList<>();
        this.grades = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void enrollStudent(Course course) {
        enrolledCourses.add(course);
    }

    public void assignGrade(Course course, double grade) {
        grades.put(course, grade);
    }

    public Map<Course, Double> getGrades() {
        return grades;
    }
}
