/******************************************************************************

                            Online Java Compiler.
                Code, Compile, Run and Debug java program online.
Write your code in this editor and press "Run" button to execute it.

************************************************************************/
import java.util.*;

class Student { String name; int id; double marks;

// Constructor
Student(String name, int id, double marks) {
    this.name = name;
    this.id = id;
    this.marks = marks;
}

// Display student details
void display() {
    System.out.println("ID: " + id + ", Name: " + name + ", Marks: " + marks);
}

}

class Main { static ArrayList<Student> students = new ArrayList<>();

// Add student
static void addStudent(String name, int id, double marks) {
    students.add(new Student(name, id, marks));
    System.out.println("Student added successfully!\n");
}

// Display all students
static void displayStudents() {
    if (students.isEmpty()) {
        System.out.println("No students available.\n");
        return;
    }
    for (Student s : students) {
        s.display();
    }
    System.out.println();
}

// Search student by ID
static void searchById(int id) {
    for (Student s : students) {
        if (s.id == id) {
            System.out.println("Student found:");
            s.display();
            return;
        }
    }
    System.out.println("Student not found.\n");
}

// Calculate average marks
static void calculateAverage() {
    if (students.isEmpty()) {
        System.out.println("No students to calculate average.\n");
        return;
    }
    double sum = 0;
    for (Student s : students) {
        sum += s.marks;
    }
    double avg = sum / students.size();
    System.out.println("Average Marks: " + avg + "\n");
}

public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int choice;

    do {
        System.out.println("===== Student Management System =====");
        System.out.println("1. Add Student");
        System.out.println("2. Display All Students");
        System.out.println("3. Search Student by ID");
        System.out.println("4. Calculate Average Marks");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");

        choice = sc.nextInt();

        switch (choice) {
            case 1:
                sc.nextLine();
                System.out.print("Enter Name: ");
                String name = sc.nextLine();
                System.out.print("Enter ID: ");
                int id = sc.nextInt();
                System.out.print("Enter Marks: ");
                double marks = sc.nextDouble();
                addStudent(name, id, marks);
                break;

            case 2:
                displayStudents();
                break;

            case 3:
                System.out.print("Enter ID to search: ");
                int searchId = sc.nextInt();
                searchById(searchId);
                break;

            case 4:
                calculateAverage();
                break;

            case 5:
                System.out.println("Exiting...");
                break;

            default:
                System.out.println("Invalid choice!\n");
        }
    } while (choice != 5);

    sc.close();
}

}