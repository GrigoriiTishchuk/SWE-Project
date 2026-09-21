package com.edujournal.backend.service;

import com.edujournal.entity.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {

    private final StudentService studentService =
            new StudentService();

    private final List<Integer> createdIds =
            new ArrayList<>();

    @AfterEach
    void cleanup() {
        for (Integer id : createdIds) {
            studentService.delete(id);
        }

        createdIds.clear();
    }

    @Test
    void saveAndFindById() {
        Student student = new Student();

        student.setStudentNumber("SERVICE001");
        student.setDateOfBirth(
                LocalDate.of(2000, 1, 1)
        );

        studentService.save(student);

        createdIds.add(student.getId());

        assertNotNull(student.getId());

        Student found =
                studentService.findById(student.getId());

        assertNotNull(found);

        assertEquals(
                "SERVICE001",
                found.getStudentNumber()
        );

        assertEquals(
                LocalDate.of(2000, 1, 1),
                found.getDateOfBirth()
        );
    }

    @Test
    void findAll() {
        List<Student> students =
                studentService.findAll();

        assertNotNull(students);
    }

    @Test
    void findByStudentNumber() {
        Student student = new Student();

        student.setStudentNumber("SERVICE002");

        studentService.save(student);

        createdIds.add(student.getId());

        Student found =
                studentService.findByStudentNumber(
                        "SERVICE002"
                );

        assertNotNull(found);

        assertEquals(
                "SERVICE002",
                found.getStudentNumber()
        );
    }

    @Test
    void update() {
        Student student = new Student();

        student.setStudentNumber("SERVICE003");
        student.setDateOfBirth(
                LocalDate.of(2000, 1, 1)
        );

        studentService.save(student);

        createdIds.add(student.getId());

        student.setStudentNumber("SERVICE003_UPDATED");
        student.setDateOfBirth(
                LocalDate.of(2001, 2, 2)
        );

        studentService.update(student);

        Student updated =
                studentService.findById(
                        student.getId()
                );

        assertNotNull(updated);

        assertEquals(
                "SERVICE003_UPDATED",
                updated.getStudentNumber()
        );

        assertEquals(
                LocalDate.of(2001, 2, 2),
                updated.getDateOfBirth()
        );
    }

    @Test
    void delete() {
        Student student = new Student();

        student.setStudentNumber("SERVICE004");
        student.setDateOfBirth(
                LocalDate.of(2000, 1, 1)
        );

        studentService.save(student);

        Integer id = student.getId();

        assertNotNull(
                studentService.findById(id)
        );

        studentService.delete(id);

        assertNull(
                studentService.findById(id)
        );
    }
}