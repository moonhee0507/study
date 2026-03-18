package service;
import model.Student;
import java.util.*;

public class StudentService {
  private List<Student> studentList = new ArrayList<>();
  private List<Runnable> listeners = new ArrayList<>();
  private int nextStudentId = 1;

  public void addListener(Runnable listener) {
    listeners.add(listener);
  }

  private void notifyListeners() {
    for (Runnable listener : listeners) {
      listener.run();
    }
  }

  public Student addStudent(String name) {
    Student student = new Student(nextStudentId++, name);
    studentList.add(student);
    notifyListeners();
    return student;
  }

  public List<Student> getAllStudents() {
    return studentList;
  }

  public Student getStudentById(int id) {
    for (Student s : studentList) {
      if (s.getStudentId() == id) return s;
    }
    return null;
  }

  public void updateStudentName(Student student, String newName) {
    if (student != null) {
      student.setStudentName(newName);
      notifyListeners();
    }
  }
}