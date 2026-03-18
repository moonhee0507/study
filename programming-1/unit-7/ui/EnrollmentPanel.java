package ui;
import javax.swing.*;
import java.awt.*;
import service.StudentService;
import service.EnrollmentService;
import model.Student;
import model.Course;

public class EnrollmentPanel extends JPanel {
  private StudentService studentService;
  private EnrollmentService enrollmentService;

  private JComboBox<Student> studentComboBox;
  private JComboBox<Course> courseComboBox;

  public EnrollmentPanel(StudentService studentService, EnrollmentService enrollmentService) {
    this.studentService = studentService;
    this.enrollmentService = enrollmentService;

    setLayout(new FlowLayout());

    studentComboBox = new JComboBox<>();
    courseComboBox = new JComboBox<>(new Course[]{
      new Course("Math"),
      new Course("Science"),
      new Course("English")
    });

    JButton enrollButton = new JButton("Enroll");

    add(new JLabel("Student:"));
    add(studentComboBox);
    add(new JLabel("Course:"));
    add(courseComboBox);
    add(enrollButton);

    enrollButton.addActionListener(e -> enrollStudent());
    courseComboBox.addItemListener(e -> refreshStudentComboBox());

    refreshStudentComboBox();
    studentService.addListener(() -> refreshStudentComboBox());
  }

  private void enrollStudent() {
    Student student = (Student) studentComboBox.getSelectedItem();
    Course course = (Course) courseComboBox.getSelectedItem();

    if (student == null || course == null) {
      JOptionPane.showMessageDialog(this, "Select student and course");
      return;
    }

    if (enrollmentService.enrollStudent(student, course)) {
      JOptionPane.showMessageDialog(this, "Student enrolled successfully");
      refreshStudentComboBox();
    } else {
      JOptionPane.showMessageDialog(this, "Already enrolled in this course");
    }
  }

  private void refreshStudentComboBox() {
    studentComboBox.removeAllItems();
    Course course = (Course) courseComboBox.getSelectedItem();

    for (Student student : studentService.getAllStudents()) {
      if (course == null || !student.getEnrolledCourses().contains(course)) {
        studentComboBox.addItem(student);
      }
    }
  }
}