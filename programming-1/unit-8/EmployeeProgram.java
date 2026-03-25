import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EmployeeProgram {
  public static void main(String[] args) {
    String fileName = "employees.csv";
    int ageThreshold = 30;

    try {
      List<Employee> employees = readEmployees(fileName);

      System.out.println("=== All Employees ===");
      employees.forEach(System.out::println);

      Function<Employee, String> nameAndDepartment =
        employee -> employee.getName() + " - " + employee.getDepartment();

      List<String> employeeInfoList = employees.stream()
        .map(nameAndDepartment)
        .collect(Collectors.toList());

      System.out.println("\n=== Name and Department Strings ===");
      employeeInfoList.forEach(System.out::println);

      double averageSalary = employees.stream()
        .mapToDouble(Employee::getSalary)
        .average()
        .orElse(0.0);

      System.out.println("\n=== Average Salary ===");
      System.out.printf("%.2f%n", averageSalary);

      List<Employee> filteredEmployees = filterEmployeesByAge(employees, ageThreshold);

      System.out.println("\n=== Employees Older Than " + ageThreshold + " ===");
      filteredEmployees.forEach(System.out::println);

    } catch (IOException e) {
      System.out.println("Error reading file: " + e.getMessage());
    }
  }

  private static List<Employee> readEmployees(String fileName) throws IOException {
    Path path = Paths.get(fileName);

    return Files.lines(path)
      .skip(1)
      .map(EmployeeProgram::parseEmployee)
      .collect(Collectors.toCollection(ArrayList::new));
  }

  private static Employee parseEmployee(String line) {
    String[] parts = line.split(",");

    String name = parts[0].trim();
    int age = Integer.parseInt(parts[1].trim());
    String department = parts[2].trim();
    double salary = Double.parseDouble(parts[3].trim());

    return new Employee(name, age, department, salary);
  }

  private static List<Employee> filterEmployeesByAge(List<Employee> employees, int ageThreshold) {
    return employees.stream()
      .filter(employee -> employee.getAge() > ageThreshold)
      .collect(Collectors.toList());
  }
}
