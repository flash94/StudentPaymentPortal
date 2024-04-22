package com.nwamara.studentportal.component;

import com.nwamara.studentportal.dao.CourseRepository;
import com.nwamara.studentportal.persistence.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final CourseRepository courseRepository;

    @Autowired
    public DataLoader(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public void run(String... args) {
        if (courseRepository.count() == 0) { // Check if there are no courses in the database
            loadSampleCourses();
        }
    }

    private void loadSampleCourses() {
        List<Course> courses = Arrays.asList(
                new Course("Java Programming", "Learn Java programming language", 5.00),
                new Course("Web Development", "Introduction to web development", 10.00),
                new Course("Database Management", "Fundamentals of database management", 15.00),
                new Course("Data Structures and Algorithms", "Advanced topics in data structures and algorithms", 5.00),
                new Course("Machine Learning", "Introduction to machine learning concepts", 5.00),
                new Course("Mobile App Development", "Building mobile applications with Android and iOS", 10.00),
                new Course("Network Security", "Security principles for computer networks", 4.00),
                new Course("Cloud Computing", "Introduction to cloud computing technologies", 5.00),
                new Course("Artificial Intelligence", "Exploring AI techniques and applications", 10.00),
                new Course("Software Engineering", "Best practices in software development", 50.00)
        );

        courseRepository.saveAll(courses);
    }
}
