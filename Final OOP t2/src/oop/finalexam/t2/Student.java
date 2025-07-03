package oop.finalexam.t2;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String name;
    private String surname;
    private String country;
    private String info;
    private List<LearningCourse> learningCourses;

    public Student(String name, String surname, String country, String info) {
        this.name = name;
        this.surname = surname;
        this.country = country;
        this.info = info;
        this.learningCourses = new ArrayList<>();
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getCountry() {
        return country;
    }

    public String getInfo() {
        return info;
    }

    public List<LearningCourse> getLearningCourses() {
        return learningCourses;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setLearningCourses(List<LearningCourse> learningCourses) {
        this.learningCourses = learningCourses;
    }

    // Method to add a course
    public void addLearningCourse(LearningCourse course) {
        this.learningCourses.add(course);
    }

    // Method to remove a course
    public void removeLearningCourse(LearningCourse course) {
        this.learningCourses.remove(course);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Student other)) return false;
        return name.equals(other.name) && surname.equals(other.surname);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + surname.hashCode();
    }
}