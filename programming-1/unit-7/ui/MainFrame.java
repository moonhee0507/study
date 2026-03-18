package ui;
import service.StudentService;
import service.EnrollmentService;
import service.GradeService;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
  public MainFrame() {
    setTitle("Student Management System");
    setSize(800, 600);
    setLayout(new BorderLayout());

    JTabbedPane tabbedPane = new JTabbedPane();

    StudentService studentService = new StudentService();
    EnrollmentService enrollmentService = new EnrollmentService();
    GradeService gradeService = new GradeService();

    tabbedPane.add("Students", new StudentPanel(studentService));
    tabbedPane.add("Enrollment", new EnrollmentPanel(studentService, enrollmentService));
    tabbedPane.add("Grades", new GradePanel(studentService, gradeService));

    add(tabbedPane, BorderLayout.CENTER);

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
  }
}