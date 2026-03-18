package ui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import service.StudentService;
import service.GradeService;
import model.Student;
import model.Course;
import util.InputValidator;

public class GradePanel extends JPanel {
  private StudentService studentService;
  private GradeService gradeService;

  private JComboBox<Student> studentComboBox;
  private JTable gradesTable;
  private DefaultTableModel gradesTableModel;
  private JComboBox<Course> courseComboBox;
  private JTextField gradeInputField;

  public GradePanel(StudentService studentService, GradeService gradeService) {
    this.studentService = studentService;
    this.gradeService = gradeService;

    setLayout(new BorderLayout());

    JPanel topPanel = new JPanel(new FlowLayout());
    studentComboBox = new JComboBox<>();
    courseComboBox = new JComboBox<>();
    gradeInputField = new JTextField(5);
    JButton assignButton = new JButton("Assign Grade");

    topPanel.add(new JLabel("Student:"));
    topPanel.add(studentComboBox);
    topPanel.add(new JLabel("Course:"));
    topPanel.add(courseComboBox);
    topPanel.add(new JLabel("Grade:"));
    topPanel.add(gradeInputField);
    topPanel.add(assignButton);
    add(topPanel, BorderLayout.NORTH);

    JPanel tablePanel = new JPanel(new BorderLayout());
    tablePanel.add(new JLabel("Enrolled courses and grades:"), BorderLayout.NORTH);
    gradesTableModel = new DefaultTableModel(new String[]{"Course", "Grade"}, 0);
    gradesTable = new JTable(gradesTableModel);
    tablePanel.add(new JScrollPane(gradesTable), BorderLayout.CENTER);
    add(tablePanel, BorderLayout.CENTER);

    studentComboBox.addItemListener(e -> onStudentSelected());
    assignButton.addActionListener(e -> assignGrade());
    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentShown(ComponentEvent e) {
        onStudentSelected();
      }
    });

    refreshStudentComboBox();
    studentService.addListener(() -> {
      refreshStudentComboBox();
      onStudentSelected();
    });
  }

  private void onStudentSelected() {
    refreshGradesTable();
    refreshCourseComboBox();
  }

  private void assignGrade() {
    Student student = (Student) studentComboBox.getSelectedItem();
    Course course = (Course) courseComboBox.getSelectedItem();
    String grade = gradeInputField.getText();

    if (student == null || course == null || InputValidator.isEmpty(grade)) {
      JOptionPane.showMessageDialog(this, "Fill all fields");
      return;
    }

    gradeService.assignGrade(student, course, grade);
    refreshGradesTable();
    gradeInputField.setText("");
    JOptionPane.showMessageDialog(this, "Grade assigned");
  }

  private void refreshStudentComboBox() {
    studentComboBox.removeAllItems();
    for (Student student : studentService.getAllStudents()) {
      studentComboBox.addItem(student);
    }
  }

  private void refreshGradesTable() {
    gradesTableModel.setRowCount(0);
    Student student = (Student) studentComboBox.getSelectedItem();
    if (student == null) return;
    for (Course course : student.getEnrolledCourses()) {
      String grade = student.getCourseGrades().getOrDefault(course.getCourseName(), "");
      gradesTableModel.addRow(new Object[]{course.getCourseName(), grade});
    }
  }

  private void refreshCourseComboBox() {
    courseComboBox.removeAllItems();
    Student student = (Student) studentComboBox.getSelectedItem();
    if (student == null) return;
    for (Course course : student.getEnrolledCourses()) {
      courseComboBox.addItem(course);
    }
  }
}