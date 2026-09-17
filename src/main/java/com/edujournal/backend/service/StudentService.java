package com.edujournal.backend.service;

import com.edujournal.dao.StudentDAO;
import com.edujournal.entity.Student;

import java.util.List;

public class StudentService {

    private final StudentDAO studentDAO;

    public StudentService() {
        this.studentDAO = new StudentDAO();
    }

    public Student findById(Integer id) {
        return studentDAO.findById(id);
    }

    public List<Student> findAll() {
        return studentDAO.findAll();
    }

    public Student findByStudentNumber(String studentNumber) {
        return studentDAO.findByStudentNumber(studentNumber);
    }

    public void save(Student student) {
        studentDAO.save(student);
    }

    public void update(Student student) {
        studentDAO.update(student);
    }

    public void delete(Integer id) {
        studentDAO.delete(id);
    }
}