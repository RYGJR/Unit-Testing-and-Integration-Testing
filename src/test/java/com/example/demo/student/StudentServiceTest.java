package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    private StudentService underTest;

    @BeforeEach
    void setUp() {

        underTest = new StudentService(studentRepository);
    }


    @Test
    void canGetAllStudents() {
        //when
        underTest.getAllStudents();
        //then
        verify(studentRepository).findAll();
    }

    @Test

    void canAddStudent() {
        //given
        Student student = new Student(
                "Hamil",
                "markhamil@gmail.com",
                Gender.MALE
        );
        //when
        underTest.addStudent(student);
        //then
        ArgumentCaptor<Student> studentArgumentCaptor =
                ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void whenEmailIsTaken() {
        //given
        Student student = new Student(
                "Hamil",
                "markhamil@gmail.com",
                Gender.MALE
        );

        given(studentRepository.selectExistsEmail(student.getEmail()))
                .willReturn(true);
        //when
        //then
        assertThatThrownBy(() ->underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " taken");
        verify(studentRepository, never()).save(any());


    }

    @Test
    void ifCanDeleteStudent() {
        //given
        long id = 10;
        given(studentRepository.existsById((id))).willReturn(true);

        //when
        underTest.deleteStudent(id);
        //then
        verify(studentRepository).deleteById(id);
    }

    @Test
    void ifCanDeleteStudentException(){
        long id = 20;
        given(studentRepository.existsById(id))
                .willReturn(false);

        assertThatThrownBy(()-> underTest.deleteStudent((id))).isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id " + id + " does not exists");
        verify(studentRepository,never()).deleteById(any());
    }
}