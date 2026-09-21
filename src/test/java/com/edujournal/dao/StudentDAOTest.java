package com.edujournal.dao;

import com.edujournal.entity.Student;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentDAOTest {

    private final StudentDAO studentDAO = new StudentDAO();

    @Test
    void saveAndFindById() {
        Student student = new Student();

        student.setStudentNumber("TEST001");
        student.setDateOfBirth(LocalDate.of(2000, 1, 1));

        studentDAO.save(student);

        assertNotNull(student.getId());

        Student found = studentDAO.findById(student.getId());

        assertNotNull(found);
        assertEquals("TEST001", found.getStudentNumber());
        assertEquals("John", found.getFirstName());
        assertEquals("Doe", found.getLastName());
        assertEquals(LocalDate.of(2000, 1, 1), found.getDateOfBirth());
    }

    @Test
    void findAll() {
        List<Student> students = studentDAO.findAll();

        assertNotNull(students);
    }

    @Test
    void findByStudentNumber() {
        Student student = new Student();

        student.setStudentNumber("TEST002");
        student.setDateOfBirth(LocalDate.of(2001, 5, 10));

        studentDAO.save(student);

        Student found =
                studentDAO.findByStudentNumber("TEST002");

        assertNotNull(found);
        assertEquals("TEST002", found.getStudentNumber());
        assertEquals("Jane", found.getFirstName());
        assertEquals("Smith", found.getLastName());
    }

    @Test
    void update() {
        Student student = new Student();

        student.setStudentNumber("TEST003");

        studentDAO.save(student);

        studentDAO.update(student);

        Student updated =
                studentDAO.findById(student.getId());

        assertNotNull(updated);
        assertEquals("NewName", updated.getFirstName());
        assertEquals("NewLastName", updated.getLastName());
    }

    @Test
    void delete() {
        Student student = new Student();

        student.setStudentNumber("TEST004");

        studentDAO.save(student);

        Integer id = student.getId();

        assertNotNull(studentDAO.findById(id));

        studentDAO.delete(id);

        assertNull(studentDAO.findById(id));
    }
}