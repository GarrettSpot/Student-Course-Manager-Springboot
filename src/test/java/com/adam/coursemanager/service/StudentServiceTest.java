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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student1;
    private Student student2;
    private Course course1;
    private Course course2;

    @BeforeEach
    void setUp() {
        student1 = new Student(1L, "Alice Smith", "alice.smith@example.com", new HashSet<>());
        student2 = new Student(2L, "Bob Johnson", "bob.j@example.com", new HashSet<>());
        course1 = new Course(101L, "Math", "Basic Math", new HashSet<>());
        course2 = new Course(102L, "Science", "Basic Science", new HashSet<>());

        student1.addCourse(course1);
        student1.addCourse(course2);
        student2.addCourse(course1);
    }

    @Test
    void whenGetAllStudents_thenReturnAllStudents() {
        // Given
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));

        // When
        List<Student> students = studentService.getAllStudents();

        // Then
        assertThat(students).hasSize(2);
        assertThat(students).contains(student1, student2);
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void whenGetStudentById_thenReturnStudent() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));

        // When
        Optional<Student> foundStudent = studentService.getStudentById(1L);

        // Then
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getName()).isEqualTo("Alice Smith");
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void whenGetStudentById_thenNotFound() {
        // Given
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<Student> foundStudent = studentService.getStudentById(99L);

        // Then
        assertThat(foundStudent).isNotPresent();
        verify(studentRepository, times(1)).findById(99L);
    }

    @Test
    void whenSaveStudent_thenStudentIsSaved() {
        // Given
        Student newStudent = new Student("Charlie Brown", "charlie.b@example.com");
        when(studentRepository.save(any(Student.class))).thenReturn(newStudent);

        // When
        Student savedStudent = studentService.saveStudent(newStudent);

        // Then
        assertThat(savedStudent.getName()).isEqualTo("Charlie Brown");
        verify(studentRepository, times(1)).save(newStudent);
    }

    @Test
    void whenDeleteStudent_thenStudentIsDeleted() {
        // Given
        doNothing().when(studentRepository).deleteById(1L);

        // When
        studentService.deleteStudent(1L);

        // Then
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void whenUpdateStudent_thenStudentIsUpdated() {
        // Given
        Student updatedDetails = new Student("Alice Wonderland", "alice.w@example.com");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
        when(studentRepository.save(any(Student.class))).thenReturn(student1);

        // When
        Student result = studentService.updateStudent(1L, updatedDetails);

        // Then
        assertThat(result.getName()).isEqualTo("Alice Wonderland");
        assertThat(result.getEmail()).isEqualTo("alice.w@example.com");
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(student1);
    }

    @Test
    void whenUpdateStudent_thenThrowsExceptionIfNotFound() {
        // Given
        Student updatedDetails = new Student("Alice Wonderland", "alice.w@example.com");
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> studentService.updateStudent(99L, updatedDetails));
        verify(studentRepository, times(1)).findById(99L);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void whenEnrollStudentInCourse_thenStudentIsEnrolled() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
        when(courseRepository.findById(101L)).thenReturn(Optional.of(course1));
        when(studentRepository.save(any(Student.class))).thenReturn(student1);

        // When
        Student result = studentService.enrollStudentInCourse(1L, 101L);

        // Then
        assertThat(result.getCourses()).contains(course1);
        verify(studentRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(101L);
        verify(studentRepository, times(1)).save(student1);
    }

    @Test
    void whenEnrollStudentInCourse_thenThrowsExceptionIfStudentNotFound() {
        // Given
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> studentService.enrollStudentInCourse(99L, 101L));
        verify(studentRepository, times(1)).findById(99L);
        verify(courseRepository, never()).findById(anyLong());
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void whenEnrollStudentInCourse_thenThrowsExceptionIfCourseNotFound() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> studentService.enrollStudentInCourse(1L, 999L));
        verify(studentRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(999L);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void whenRemoveStudentFromCourse_thenStudentIsRemoved() {
        // Given
        student1.setCourses(new HashSet<>(Arrays.asList(course1, course2))); // Ensure student1 has both courses
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
        when(courseRepository.findById(101L)).thenReturn(Optional.of(course1));
        when(studentRepository.save(any(Student.class))).thenReturn(student1);

        // When
        Student result = studentService.removeStudentFromCourse(1L, 101L);

        // Then
        assertThat(result.getCourses()).doesNotContain(course1);
        assertThat(result.getCourses()).contains(course2); // Should still contain course2
        verify(studentRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(101L);
        verify(studentRepository, times(1)).save(student1);
    }

    @Test
    void whenRemoveStudentFromCourse_thenThrowsExceptionIfStudentNotFound() {
        // Given
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> studentService.removeStudentFromCourse(99L, 101L));
        verify(studentRepository, times(1)).findById(99L);
        verify(courseRepository, never()).findById(anyLong());
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void whenRemoveStudentFromCourse_thenThrowsExceptionIfCourseNotFound() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> studentService.removeStudentFromCourse(1L, 999L));
        verify(studentRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(999L);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void whenGetAllStudentsWithCourses_thenReturnStudentsWithCourses() {
        // Given
        when(studentRepository.findAllStudentsWithCourses()).thenReturn(Arrays.asList(student1, student2));

        // When
        List<Student> students = studentService.getAllStudentsWithCourses();

        // Then
        assertThat(students).hasSize(2);
        assertThat(students.get(0).getCourses()).hasSize(2); // student1 has 2 courses
        assertThat(students.get(1).getCourses()).hasSize(1); // student2 has 1 course
        verify(studentRepository, times(1)).findAllStudentsWithCourses();
    }
}
