University Management System (UMS)

Student: Giorgi Maisuradze  
Course: Object-Oriented Programming  
Package: oop.finalexam.t2

What This Program Does

This is a simple University Management System that manages students and their courses. The program can store student information and show what courses each student is taking.

Files in the Project

- LearningCourse.java - Represents a university course
- Student.java - Represents a student with their courses
- UMS.java - Main system that manages all students
- Main.java - Runs the program and shows examples

How to Run

1. Compile all Java files:
   javac oop/finalexam/t2/.java

2. Run the program:
   java oop.finalexam.t2.Main

What the Program Shows

The program creates three students:
- Giorgi Maisuradze (myself) - with my actual courses from Argus
- Jon Smith - example international student
- Luka Beridze - example local student

For each student, it prints:
- Personal information (name, country, description)
- List of all courses they are taking
- Course details (title, prerequisites, topics)

Key Features

- Add/Remove Students: Can add new students to the system
- Add/Remove Courses: Students can enroll in or drop courses
- Display Information: Shows complete student and course information
- Real Course Data: Uses my actual courses from the university system

Classes Explanation

LearningCourse
- Stores course title, prerequisites, and major topics
- Has getter/setter methods and toString() for display

Student
- Stores name, surname, country, and personal info
- Has a list of courses the student is taking
- Can add/remove courses

UMS (University Management System)
- Manages multiple students
- Has a special method to load my real courses
- Can print detailed student information

Sample Output

=== STUDENT INFORMATION ===
Name: Giorgi
Surname: Maisuradze
Country: Georgia
Info: First year programming student

=== LEARNING COURSES FOR Giorgi Maisuradze ===
1. Course: Object-Oriented Programming
  Prerequisites: Introduction to Programming (ENG)
  Major Topics: Java syntax and data structures;
Procedural programming
Classes
Encapsulation, polymorphism, inheritance

OOP Concepts Used

- Encapsulation: Private variables with public methods
- Composition: Student contains LearningCourse objects
- ArrayList: To store multiple students and courses
- toString(): For easy printing of objects
- equals(): For comparing students properly