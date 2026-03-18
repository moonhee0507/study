package service;
import model.Student;
import model.Course;

public class EnrollmentService {
  /** @return true if enrolled, false if already enrolled */
  public boolean enrollStudent(Student student, Course course) {
    if (student.getEnrolledCourses().contains(course)) {
      return false;
    }
    student.getEnrolledCourses().add(course);
    return true;
  }
}