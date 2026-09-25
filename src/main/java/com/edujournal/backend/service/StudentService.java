package com.edujournal.backend.service;

import com.edujournal.backend.utils.GeneratorUtil;
import com.edujournal.backend.utils.StudentMapper;
import com.edujournal.dao.EnrollmentDAO;
import com.edujournal.dao.GradesDAO;
import com.edujournal.dao.StudentDAO;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.Enrollment;
import com.edujournal.entity.Role;
import com.edujournal.entity.Student;
import com.edujournal.entity.User;
import com.edujournal.model.StudentDTO;

import java.util.List;
import java.util.stream.Collectors;

public class StudentService {

    private final StudentDAO studentDAO;
    private final UserDAO userDAO;
    private final StudentMapper studentMapper;
    private final EnrollmentDAO enrollmentDAO;
    private final GradesDAO gradesDAO;
    private final UserService userService;

    public StudentService() {
        this.studentDAO = new StudentDAO();
        this.userDAO = new UserDAO();
        this.studentMapper = new StudentMapper();
        this.enrollmentDAO = new EnrollmentDAO();
        this.gradesDAO = new GradesDAO();
        this.userService = new UserService();
    }

    public Student findById(Integer id) {
        return studentDAO.findById(id);
    }

    public Student findByUserId(Integer userId) {
        return studentDAO.findByUserId(userId);
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

    public void delete(Integer studentId) {
        List<Enrollment> enrollments = enrollmentDAO.findByStudentId(studentId);

        if (!enrollments.isEmpty()) {
            for (Enrollment e : enrollments) {
                if (!gradesDAO.findByEnrollment(e.getId()).isEmpty()) {
                    throw new IllegalStateException(
                            "Cannot delete student: grades exist."
                    );
                }
            }

            for (Enrollment e : enrollments) {
                if (e.getCourseId() != null) {
                    throw new IllegalStateException(
                            "Cannot delete student: enrolled in courses."
                    );
                }
            }
        }
        studentDAO.delete(studentId);
    }

    public StudentDTO findDTOById(Integer studentId) {
        Student student = studentDAO.findById(studentId);
        if (student == null) return null;

        User user = userDAO.findById(student.getUserId());
        return studentMapper.toDTO(user, student);
    }

    public List<StudentDTO> findAllDTO() {
        return studentDAO.findAll()
                .stream()
                .map(student -> {
                    User user = userDAO.findById(student.getUserId());
                    return studentMapper.toDTO(user, student);
                })
                .collect(Collectors.toList());
    }

    public StudentDTO createStudent(String firstName, String lastName, String phone) {

        User user = userService.createStudentUser(firstName, lastName, phone);

        String studentNumber;
        do {
            studentNumber = GeneratorUtil.generateStudentNumber();
        } while (studentDAO.findByStudentNumber(studentNumber) != null);

        Student student = new Student();
        student.setUserId(user.getId());
        student.setStudentNumber(studentNumber);

        studentDAO.save(student);

        return studentMapper.toDTO(user, student);
    }

    public void updateFromDTO(StudentDTO dto) {
        Student student = studentDAO.findById(dto.getStudentId());
        if (student == null) return;

        student.setStudentNumber(dto.getStudentNumber());
        student.setAcademicGroupId(dto.getAcademicGroupId());

        studentDAO.update(student);

        User user = userDAO.findById(dto.getUserId());
        if (user != null) {
            user.setUsername(dto.getUsername());
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            userDAO.update(user);
        }
    }

    public String generateUniqueStudentNumber() {

        String number;

        do {
            number = GeneratorUtil.generateStudentNumber();
        } while (studentDAO.findByStudentNumber(number) != null);

        return number;
    }

}