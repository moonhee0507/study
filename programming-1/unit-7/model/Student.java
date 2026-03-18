package model;

import java.util.*;

public class Student {
  private int studentId;
  private String studentName;
  private List<Course> enrolledCourses = new ArrayList<>();
  private Map<String, String> courseGrades = new HashMap<>();

  public Student(int studentId, String studentName) {
    this.studentId = studentId;
    this.studentName = studentName;
  }

  public int getStudentId() {
    return studentId;
  }

  public String getStudentName() {
    return studentName;
  }

  public void setStudentName(String studentName) {
    this.studentName = studentName;
  }

  public List<Course> getEnrolledCourses() {
    return enrolledCourses;
  }

  public Map<String, String> getCourseGrades() {
    return courseGrades;
  }

  @Override
  public String toString() {
    return studentName;
  }
}