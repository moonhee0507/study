package ui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import service.StudentService;
import util.InputValidator;
import model.Student;
import model.Course;

public class StudentPanel extends JPanel {
  private StudentService studentService;
  private JTextField nameInputField;
  private JTable studentTable;
  private DefaultTableModel tableModel;

  public StudentPanel(StudentService studentService) {
    this.studentService = studentService;

    setLayout(new BorderLayout());

    JPanel inputPanel = new JPanel();
    nameInputField = new JTextField(15);
    JButton addButton = new JButton("Add Student");
    JButton updateButton = new JButton("Update Student");
    JButton viewButton = new JButton("View Student Details");

    inputPanel.add(new JLabel("Name:"));
    inputPanel.add(nameInputField);
    inputPanel.add(addButton);
    inputPanel.add(updateButton);
    inputPanel.add(viewButton);

    add(inputPanel, BorderLayout.NORTH);

    tableModel = new DefaultTableModel(new String[]{"ID", "Name"}, 0);
    studentTable = new JTable(tableModel);
    add(new JScrollPane(studentTable), BorderLayout.CENTER);

    addButton.addActionListener(e -> addStudent());
    updateButton.addActionListener(e -> updateStudent());
    viewButton.addActionListener(e -> viewStudentDetails());
  }

  private void addStudent() {
    String name = nameInputField.getText();

    if (InputValidator.isEmpty(name)) {
      JOptionPane.showMessageDialog(this, "Name cannot be empty");
      return;
    }

    studentService.addStudent(name);
    refreshTable();
    nameInputField.setText("");
  }

  private void refreshTable() {
    tableModel.setRowCount(0);

    for (Student student : studentService.getAllStudents()) {
      tableModel.addRow(new Object[]{
        student.getStudentId(),
        student.getStudentName()
      });
    }
  }

  private void updateStudent() {
    int row = studentTable.getSelectedRow();
    if (row < 0) {
      JOptionPane.showMessageDialog(this, "Select a student");
      return;
    }
    int id = (Integer) tableModel.getValueAt(row, 0);
    Student student = studentService.getStudentById(id);
    if (student == null) return;

    String newName = JOptionPane.showInputDialog(this, "New name:", student.getStudentName());
    if (newName == null) return;
    if (InputValidator.isEmpty(newName)) {
      JOptionPane.showMessageDialog(this, "Name cannot be empty");
      return;
    }
    studentService.updateStudentName(student, newName);
    refreshTable();
  }

  private void viewStudentDetails() {
    int row = studentTable.getSelectedRow();
    if (row < 0) {
      JOptionPane.showMessageDialog(this, "Select a student");
      return;
    }
    int id = (Integer) tableModel.getValueAt(row, 0);
    Student student = studentService.getStudentById(id);
    if (student == null) return;

    StringBuilder msg = new StringBuilder();
    msg.append("ID: ").append(student.getStudentId()).append("\n");
    msg.append("Name: ").append(student.getStudentName()).append("\n\n");
    msg.append("Enrolled courses: ");
    if (student.getEnrolledCourses().isEmpty()) {
      msg.append("(none)");
    } else {
      for (Course c : student.getEnrolledCourses()) {
        msg.append(c.getCourseName()).append(" ");
      }
    }
    msg.append("\n\nGrades: ");
    if (student.getCourseGrades().isEmpty()) {
      msg.append("(none)");
    } else {
      student.getCourseGrades().forEach((course, grade) ->
        msg.append(course).append("=").append(grade).append(" "));
    }
    JOptionPane.showMessageDialog(this, msg.toString(), "Student Details", JOptionPane.INFORMATION_MESSAGE);
  }
}