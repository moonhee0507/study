package model;

public class Course {
  private String courseName;

  public Course(String courseName) {
    this.courseName = courseName;
  }

  public String getCourseName() {
    return courseName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Course)) return false;
    Course course = (Course) o;
    return java.util.Objects.equals(courseName, course.courseName);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(courseName);
  }

  @Override
  public String toString() {
    return courseName;
  }
}