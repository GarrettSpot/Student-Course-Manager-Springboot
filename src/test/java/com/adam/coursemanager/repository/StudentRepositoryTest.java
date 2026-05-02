package com.adam.coursemanager.repository;

import com.adam.coursemanager.entity.Course;
import com.adam.coursemanager.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestEntityManager entityManager; // Used to persist entities directly for testing

    private Student student1;
    private Student student2;
    private Course course1;
    private Course course2;

    @BeforeEach
    void setUp() {
        // Clear database before each test
        entityManager.clear();
        studentRepository.deleteAll();

        // Create sample data
        student1 = new Student("Alice Smith", "alice.smith@example.com");
        student2 = new Student("Bob Johnson", "bob.j@example.com");

        course1 = new Course("Math", "Basic Math");
        course2 = new Course("Science", "Basic Science");

        // Persist entities
        entityManager.persist(course1);
        entityManager.persist(course2);
        entityManager.flush(); // Ensure courses are saved before associating

        student1.addCourse(course1);
        student1.addCourse(course2);
        student2.addCourse(course1);

        entityManager.persist(student1);
        entityManager.persist(student2);
        entityManager.flush(); // Ensure students and their associations are saved
    }

    @Test
    void whenFindByEmail_thenReturnStudent() {
        // When
        Optional<Student> foundStudent = studentRepository.findByEmail("alice.smith@example.com");

        // Then
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getName()).isEqualTo("Alice Smith");
    }

    @Test
    void whenFindByEmail_thenNotFound() {
        // When
        Optional<Student> foundStudent = studentRepository.findByEmail("nonexistent@example.com");

        // Then
        assertThat(foundStudent).isNotPresent();
    }

    @Test
    void whenFindAllStudentsWithCourses_thenReturnAllStudentsWithCourses() {
        // When
        List<Student> students = studentRepository.findAllStudentsWithCourses();

        // Then
        assertThat(students).hasSize(2); // student1 and student2

        // Verify student1's courses
        Optional<Student> foundStudent1 = students.stream()
                .filter(s -> s.getEmail().equals("alice.smith@example.com"))
                .findFirst();
        assertThat(foundStudent1).isPresent();
        Set<Course> student1Courses = foundStudent1.get().getCourses();
        assertThat(student1Courses).hasSize(2);
        assertThat(student1Courses).extracting(Course::getTitle).containsExactlyInAnyOrder("Math", "Science");

        // Verify student2's courses
        Optional<Student> foundStudent2 = students.stream()
                .filter(s -> s.getEmail().equals("bob.j@example.com"))
                .findFirst();
        assertThat(foundStudent2).isPresent();
        Set<Course> student2Courses = foundStudent2.get().getCourses();
        assertThat(student2Courses).hasSize(1);
        assertThat(student2Courses).extracting(Course::getTitle).containsExactlyInAnyOrder("Math");
    }

    @Test
    void whenSaveStudent_thenStudentIsPersisted() {
        // Given
        Student newStudent = new Student("New Student", "new.student@example.com");

        // When
        Student savedStudent = studentRepository.save(newStudent);
        entityManager.flush(); // Ensure changes are written to the database

        // Then
        assertThat(savedStudent.getId()).isNotNull();
        Optional<Student> foundStudent = studentRepository.findById(savedStudent.getId());
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getEmail()).isEqualTo("new.student@example.com");
    }

    @Test
    void whenDeleteStudent_thenStudentIsRemoved() {
        // Given
        Long student1Id = student1.getId();

        // When
        studentRepository.deleteById(student1Id);
        entityManager.flush(); // Ensure deletion is written to the database

        // Then
        Optional<Student> foundStudent = studentRepository.findById(student1Id);
        assertThat(foundStudent).isNotPresent();
    }
}
