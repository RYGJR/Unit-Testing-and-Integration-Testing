package com.example.demo.student;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StudentRepositoryTest {


    @Autowired
    private StudentRepository underTest;



    @Test
    void itShouldCheckIfStudentExistsEmail() {
        //given
        String email = "markhamil@gmail.com";
        Student student = new Student(
                "Hamil",
                email,
                Gender.MALE
        );
        underTest.save(student);

        //when
      boolean expected =  underTest.selectExistsEmail(email);
        //then
        assertThat(expected).isTrue();
    }
}