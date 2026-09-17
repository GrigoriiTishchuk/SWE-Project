package com.edujournal.backend.service;

import com.edujournal.entity.Student;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {

    private final StudentService studentService = new StudentService();

    @Test
    void saveAndFindById() {
        Student student = new Student();

        student.setStudentNumber("SERVICE001");
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setDateOfBirth(LocalDate.of(2000, 1, 1));

        studentService.save(student);

        assertNotNull(student.getId());

        Student found = studentService.findById(student.getId());

        assertNotNull(found);
        assertEquals("SERVICE001", found.getStudentNumber());
        assertEquals("John", found.getFirstName());
        assertEquals("Doe", found.getLastName());
    }

    @Test
    void findAll() {
        List<Student> students = studentService.findAll();

        assertNotNull(students);
    }

    @Test
    void findByStudentNumber() {
        Student student = new Student();

        student.setStudentNumber("SERVICE002");
        student.setFirstName("Jane");
        student.setLastName("Smith");

        studentService.save(student);

        Student found =
                studentService.findByStudentNumber("SERVICE002");

        assertNotNull(found);
        assertEquals("SERVICE002", found.getStudentNumber());
        assertEquals("Jane", found.getFirstName());
    }

    @Test
    void update() {
        Student student = new Student();

        student.setStudentNumber("SERVICE003");
        student.setFirstName("OldName");
        student.setLastName("OldLastName");

        studentService.save(student);

        student.setFirstName("NewName");
        student.setLastName("NewLastName");

        studentService.update(student);

        Student updated =
                studentService.findById(student.getId());

        assertNotNull(updated);
        assertEquals("NewName", updated.getFirstName());
        assertEquals("NewLastName", updated.getLastName());
    }

    @Test
    void delete() {
        Student student = new Student();

        student.setStudentNumber("SERVICE004");
        student.setFirstName("Delete");
        student.setLastName("Test");

        studentService.save(student);

        Integer id = student.getId();

        assertNotNull(studentService.findById(id));

        studentService.delete(id);

        assertNull(studentService.findById(id));
    }
}