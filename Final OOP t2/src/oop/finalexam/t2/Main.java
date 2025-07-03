package oop.finalexam.t2;

public class Main {
    public static void main(String[] args) {
        // Create UMS instance
        UMS ums = new UMS();

        // Create students
        Student me = new Student("Giorgi", "Maisuradze", "Georgia", "Firs year programming student");
        Student other = new Student("Jon", "Smith", "UK", "International student");
        Student another = new Student("Luka", "Beridze", "Georgia", "Local student interested in AI");

        // Add students to UMS
        ums.addStudent(me);
        ums.addStudent(other);
        ums.addStudent(another);

        // Set up your actual courses from Argus
        ums.setupMyActualCourses(me);

        // Add some courses for other students
        other.addLearningCourse(new LearningCourse(
                "Introduction to Programming",
                "No prerequisites",
                "Basic programming in python, Variables, HTML, Control structures"
        ));

        other.addLearningCourse(new LearningCourse(
                "Mathematics for Computer Science",
                "High school mathematics",
                "Discrete mathematics, Logic, Set theory, Probability"
        ));

        another.addLearningCourse(new LearningCourse(
                "Artificial Intelligence",
                "Programming knowledge, Statistics",
                "Machine learning, Neural networks, Expert systems, Natural language processing"
        ));

        // Print student data using the required method
        System.out.println("Printing data for Giorgi Maisuradze (yourself):");
        ums.printStudentData(me);

        System.out.println("\nPrinting data for Ana Smith:");
        ums.printStudentData(other);

        System.out.println("\nPrinting data for Luka Beridze:");
        ums.printStudentData(another);
    }
}