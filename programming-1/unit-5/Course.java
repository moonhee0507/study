public class Course {
    private String courseCode;
    private String courseName;
    private int maximumCapacity;
    private int enrolledCount;
    private static int totalEnrolledStudents = 0;

    public Course(String courseCode, String courseName, int maximumCapacity) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.maximumCapacity = maximumCapacity;
        this.enrolledCount = 0;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    public int getEnrolledCount() {
        return enrolledCount;
    }

    public boolean enrollStudent() {
        if (enrolledCount < maximumCapacity) {
            enrolledCount++;
            totalEnrolledStudents++;
            return true;
        }
        return false;
    }

    public static int getTotalEnrolledStudents() {
        return totalEnrolledStudents;
    }
}
