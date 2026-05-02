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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Course course1;
    private Course course2;
    private Student student1;

    @BeforeEach
    void setUp() {
        // Clear database before each test
        entityManager.clear();
        courseRepository.deleteAll();

        // Create sample data
        course1 = new Course("Math", "Basic Math");
        course2 = new Course("Science", "Basic Science");
        student1 = new Student("Alice", "alice@example.com");

        // Persist entities
        entityManager.persist(student1);
        entityManager.persist(course1);
        entityManager.persist(course2);
        entityManager.flush();

        // Establish a relationship
        student1.addCourse(course1);
        entityManager.persist(student1);
        entityManager.flush();
    }

    @Test
    void whenFindByTitle_thenReturnCourse() {
        // When
        Optional<Course> foundCourse = courseRepository.findByTitle("Math");

        // Then
        assertThat(foundCourse).isPresent();
        assertThat(foundCourse.get().getDescription()).isEqualTo("Basic Math");
    }

    @Test
    void whenFindByTitle_thenNotFound() {
        // When
        Optional<Course> foundCourse = courseRepository.findByTitle("Nonexistent Course");

        // Then
        assertThat(foundCourse).isNotPresent();
    }

    @Test
    void whenFindAll_thenReturnAllCourses() {
        // When
        List<Course> courses = courseRepository.findAll();

        // Then
        assertThat(courses).hasSize(2);
        assertThat(courses).extracting(Course::getTitle).containsExactlyInAnyOrder("Math", "Science");
    }

    @Test
    void whenSaveCourse_thenCourseIsPersisted() {
        // Given
        Course newCourse = new Course("History", "World History");

        // When
        Course savedCourse = courseRepository.save(newCourse);
        entityManager.flush();

        // Then
        assertThat(savedCourse.getId()).isNotNull();
        Optional<Course> foundCourse = courseRepository.findById(savedCourse.getId());
        assertThat(foundCourse).isPresent();
        assertThat(foundCourse.get().getTitle()).isEqualTo("History");
    }

    @Test
    void whenDeleteCourse_thenCourseIsRemoved() {
        // Given
        Long course1Id = course1.getId();

        // When
        courseRepository.deleteById(course1Id);
        entityManager.flush();

        // Then
        Optional<Course> foundCourse = courseRepository.findById(course1Id);
        assertThat(foundCourse).isNotPresent();

        // Verify that the relationship is also removed from the student side
        Student updatedStudent = entityManager.find(Student.class, student1.getId());
        assertThat(updatedStudent.getCourses()).doesNotContain(course1);
    }
}
