package com.adam.coursemanager;

import com.adam.coursemanager.entity.Course;
import com.adam.coursemanager.entity.Student;
import com.adam.coursemanager.service.CourseService;
import com.adam.coursemanager.service.StudentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class CoursemanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoursemanagerApplication.class, args);
    }

    @Bean
    @ConditionalOnBean({StudentService.class, CourseService.class})
    public CommandLineRunner demoData(StudentService studentService, CourseService courseService) {
        return args -> {
            // Check if data already exists to prevent duplicate insertions on restart
            if (studentService.getAllStudents().isEmpty() && courseService.getAllCourses().isEmpty()) {
                System.out.println("Populating initial data...");

                // Create sample students
                Student s1 = new Student("Alice Smith", "alice.smith@example.com");
                Student s2 = new Student("Bob Johnson", "bob.j@example.com");
                Student s3 = new Student("Charlie Brown", "charlie.b@example.com");
                Student s4 = new Student("Diana Prince", "diana.p@example.com");
                Student s5 = new Student("Eve Adams", "eve.a@example.com");
                Student s6 = new Student("Frank White", "frank.w@example.com");
                Student s7 = new Student("Grace Black", "grace.b@example.com");
                Student s8 = new Student("Heidi Green", "heidi.g@example.com");
                Student s9 = new Student("Ivan Blue", "ivan.bl@example.com");
                Student s10 = new Student("Judy Red", "judy.r@example.com");

                List<Student> students = Arrays.asList(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10);
                students.forEach(studentService::saveStudent);

                // Create sample courses
                Course c1 = new Course("Introduction to Programming", "Learn the basics of coding.");
                Course c2 = new Course("Web Development Fundamentals", "HTML, CSS, JavaScript basics.");
                Course c3 = new Course("Database Management", "SQL and relational databases.");
                Course c4 = new Course("Spring Boot Microservices", "Building microservices with Spring Boot.");
                Course c5 = new Course("Advanced Java", "Deep dive into Java concepts.");
                Course c6 = new Course("Data Structures and Algorithms", "Essential for problem solving.");
                Course c7 = new Course("Machine Learning Basics", "Introduction to ML algorithms.");
                Course c8 = new Course("Cloud Computing with AWS", "Deploying applications on AWS.");
                Course c9 = new Course("Mobile App Development", "Building Android applications.");
                Course c10 = new Course("Cybersecurity Essentials", "Understanding security principles.");

                List<Course> courses = Arrays.asList(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10);
                courses.forEach(courseService::saveCourse);

                // Enroll students in courses
                s1.addCourse(c1); s1.addCourse(c2);
                s2.addCourse(c2); s2.addCourse(c3);
                s3.addCourse(c1); s3.addCourse(c3); s3.addCourse(c4);
                s4.addCourse(c5); s4.addCourse(c6);
                s5.addCourse(c1); s5.addCourse(c7);
                s6.addCourse(c8); s6.addCourse(c9);
                s7.addCourse(c10); s7.addCourse(c1);
                s8.addCourse(c2); s8.addCourse(c4);
                s9.addCourse(c3); s9.addCourse(c5);
                s10.addCourse(c6); s10.addCourse(c7);

                // Persist associations
                students.forEach(studentService::saveStudent);
                System.out.println("Initial data population complete.");
            } else {
                System.out.println("Database already contains data. Skipping initial data population.");
            }

            System.out.println("\nApplication is running! Access it at: http://localhost:8080/students");
            System.out.println("Or for courses: http://localhost:8080/courses\n");
        };
    }
}
