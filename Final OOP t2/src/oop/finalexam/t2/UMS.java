package oop.finalexam.t2;

import java.util.ArrayList;
import java.util.List;

public class UMS {
    private List<Student> students;

    public UMS() {
        this.students = new ArrayList<>();
    }

    // Method to add a student
    public void addStudent(Student student) {
        students.add(student);
    }

    // Method to remove a student
    public void removeStudent(Student student) {
        students.remove(student);
    }

    // Method to get all students
    public List<Student> getStudents() {
        return students;
    }

    // Required method to print student data
    public void printStudentData(Student student) {
        System.out.println("=== STUDENT INFORMATION ===");
        System.out.println("Name: " + student.getName());
        System.out.println("Surname: " + student.getSurname());
        System.out.println("Country: " + student.getCountry());
        System.out.println("Info: " + student.getInfo());
        System.out.println();

        System.out.println("=== LEARNING COURSES FOR " + student.getName() + " " + student.getSurname() + " ===");
        if (student.getLearningCourses().isEmpty()) {
            System.out.println("No courses enrolled.");
        } else {
            for (int i = 0; i < student.getLearningCourses().size(); i++) {
                System.out.println((i + 1) + ". " + student.getLearningCourses().get(i));
                System.out.println();
            }
        }
        System.out.println("===============================");
    }

    // Helper method to set up your actual courses from Argus
    public void setupMyActualCourses(Student student) {
        // Check if this is you (Giorgi Maisuradze)
        if (student.getName().equals("Giorgi") && student.getSurname().equals("Maisuradze")) {
            // Add your actual courses from Argus - replace these with your real courses
            student.addLearningCourse(new LearningCourse(
                    "Object-Oriented Programming",
                    "Introduction to Programming (ENG)",
                    "Java syntax and data structures;\n" +
                            "Procedural programming\n" +
                            "Classes\n" +
                            "Encapsulation, polymorphism, inheritance\n" +
                            "Packages\n" +
                            "Working with the network\n" +
                            "Work with files\n" +
                            "Working with text data\n" +
                            "Work with the terminal."
            ));

            student.addLearningCourse(new LearningCourse(
                    "Cyberlaw and Ethics (ENG)",
                    "Dont have",
                    "Elementary Human Rights;\n" +
                            "Intellectual Property;\n" +
                            "Legal and Ethical Regulations."
            ));

            student.addLearningCourse(new LearningCourse(
                    "Introduction to Programming (ENG)",
                    "Basic computer science knowledge",
                    "Representation and manipulation of the information in the computer\n" +
                            "Algorithmical thinking and correct perception of the data structures\n" +
                            "Programming Languages, compilers and interpreters\n" +
                            "Concepts of how computer networks and internet works and data is represented in the web\n" +
                            "Outcomes"
            ));

            student.addLearningCourse(new LearningCourse(
                    "Calculus I",
                    "Dont have",
                    "Numerical sequences;\n" +
                            "Numerical functions;\n" +
                            "Limit and Continuity;\n" +
                            "Derivatives;\n" +
                            "Differentials;"
            ));

        }
    }
}