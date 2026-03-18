package service;
import model.Student;
import model.Course;

public class GradeService {
  public void assignGrade(Student student, Course course, String grade) {
    student.getCourseGrades().put(course.getCourseName(), grade);
  }
}