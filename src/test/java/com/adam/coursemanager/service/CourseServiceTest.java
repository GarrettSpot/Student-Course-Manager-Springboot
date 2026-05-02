package com.adam.coursemanager.service;

import com.adam.coursemanager.entity.Course;
import com.adam.coursemanager.entity.Student;
import com.adam.coursemanager.repository.CourseRepository;
import com.adam.coursemanager.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course1;
    private Course course2;
    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        course1 = new Course(101L, "Math", "Basic Math", new HashSet<>());
        course2 = new Course(102L, "Science", "Basic Science", new HashSet<>());
        student1 = new Student(1L, "Alice Smith", "alice.smith@example.com", new HashSet<>());
        student2 = new Student(2L, "Bob Johnson", "bob.j@example.com", new HashSet<>());

        // Establish relationships for testing delete
        student1.addCourse(course1);
        student2.addCourse(course1);
        course1.getStudents().add(student1);
        course1.getStudents().add(student2);
    }

    @Test
    void whenGetAllCourses_thenReturnAllCourses() {
        // Given
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));

        // When
        List<Course> courses = courseService.getAllCourses();

        // Then
        assertThat(courses).hasSize(2);
        assertThat(courses).contains(course1, course2);
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void whenGetCourseById_thenReturnCourse() {
        // Given
        when(courseRepository.findById(101L)).thenReturn(Optional.of(course1));

        // When
        Optional<Course> foundCourse = courseService.getCourseById(101L);

        // Then
        assertThat(foundCourse).isPresent();
        assertThat(foundCourse.get().getTitle()).isEqualTo("Math");
        verify(courseRepository, times(1)).findById(101L);
    }

    @Test
    void whenGetCourseById_thenNotFound() {
        // Given
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Course> foundCourse = courseService.getCourseById(999L);

        // Then
        assertThat(foundCourse).isNotPresent();
        verify(courseRepository, times(1)).findById(999L);
    }

    @Test
    void whenSaveCourse_thenCourseIsSaved() {
        // Given
        Course newCourse = new Course("History", "World History");
        when(courseRepository.save(any(Course.class))).thenReturn(newCourse);

        // When
        Course savedCourse = courseService.saveCourse(newCourse);

        // Then
        assertThat(savedCourse.getTitle()).isEqualTo("History");
        verify(courseRepository, times(1)).save(newCourse);
    }

    @Test
    void whenDeleteCourse_thenCourseIsRemovedAndStudentsUpdated() {
        // Given
        when(courseRepository.findById(101L)).thenReturn(Optional.of(course1));
        when(studentRepository.save(any(Student.class))).thenReturn(student1, student2); // Mock saving students

        // When
        courseService.deleteCourse(101L);

        // Then
        verify(courseRepository, times(1)).findById(101L);
        verify(studentRepository, times(2)).save(any(Student.class)); // student1 and student2 are saved
        verify(courseRepository, times(1)).delete(course1);

        // Verify students no longer have the course
        assertThat(student1.getCourses()).doesNotContain(course1);
        assertThat(student2.getCourses()).doesNotContain(course1);
    }

    @Test
    void whenDeleteCourse_thenThrowsExceptionIfNotFound() {
        // Given
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> courseService.deleteCourse(999L));
        verify(courseRepository, times(1)).findById(999L);
        verify(studentRepository, never()).save(any(Student.class));
        verify(courseRepository, never()).delete(any(Course.class));
    }

    @Test
    void whenUpdateCourse_thenCourseIsUpdated() {
        // Given
        Course updatedDetails = new Course("Advanced Math", "Complex Math Topics");
        when(courseRepository.findById(101L)).thenReturn(Optional.of(course1));
        when(courseRepository.save(any(Course.class))).thenReturn(course1);

        // When
        Course result = courseService.updateCourse(101L, updatedDetails);

        // Then
        assertThat(result.getTitle()).isEqualTo("Advanced Math");
        assertThat(result.getDescription()).isEqualTo("Complex Math Topics");
        verify(courseRepository, times(1)).findById(101L);
        verify(courseRepository, times(1)).save(course1);
    }

    @Test
    void whenUpdateCourse_thenThrowsExceptionIfNotFound() {
        // Given
        Course updatedDetails = new Course("Advanced Math", "Complex Math Topics");
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> courseService.updateCourse(999L, updatedDetails));
        verify(courseRepository, times(1)).findById(999L);
        verify(courseRepository, never()).save(any(Course.class));
    }
}
