public class Student {

    // Private instance variables
    private String name;
    private String id;
    private int age;
    private String grade;

    // Constructor
    public Student(String id, String name, int age, String grade) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.grade = grade;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    // Display student information
    @Override
    public String toString() {
        return "Student ID: " + id +
               "\nName: " + name +
               "\nAge: " + age +
               "\nGrade: " + grade;
    }
}
