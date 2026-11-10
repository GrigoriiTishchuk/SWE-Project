package com.edujournal.backend.service;

import com.edujournal.backend.utils.GeneratorUtil;
import com.edujournal.backend.utils.StudentMapper;
import com.edujournal.dao.EnrollmentDAO;
import com.edujournal.dao.GradesDAO;
import com.edujournal.dao.StudentDAO;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.Enrollment;
import com.edujournal.entity.Student;
import com.edujournal.entity.User;
import com.edujournal.model.StudentDTO;

import java.util.List;

/**
 * Provides business logic for managing student records
 * and coordinating student-related data operations.
 *
 * @author EduJournal
 */
public final class StudentService {

    /** Data access object for student operations. */
    private final StudentDAO studentDAO;

    /** Data access object for user operations. */
    private final UserDAO userDAO;

    /** Mapper for converting student entities into DTOs. */
    private final StudentMapper studentMapper;

    /** Data access object for enrollment operations. */
    private final EnrollmentDAO enrollmentDAO;

    /** Data access object for grade operations. */
    private final GradesDAO gradesDAO;

    /** Service for managing user-related operations. */
    private final UserService userService;

    /**
     * Creates a student service and initializes its dependencies.
     */
    public StudentService() {
        this.studentDAO = new StudentDAO();
        this.userDAO = new UserDAO();
        this.studentMapper = new StudentMapper();
        this.enrollmentDAO = new EnrollmentDAO();
        this.gradesDAO = new GradesDAO();
        this.userService = new UserService();
    }

    /**
     * Finds a student by their unique identifier.
     *
     * @param id the student identifier
     * @return the matching student, or null if not found
     */
    public Student findById(final Integer id) {
        return studentDAO.findById(id);
    }

    /**
     * Finds a student by their associated user identifier.
     *
     * @param userId the user identifier
     * @return the matching student, or null if not found
     */
    public Student findByUserId(final Integer userId) {
        return studentDAO.findByUserId(userId);
    }

    /**
     * Retrieves all student records.
     *
     * @return a list of all students
     */
    public List<Student> findAll() {
        return studentDAO.findAll();
    }

    /**
     * Finds a student by their student number.
     *
     * @param studentNumber the student number
     * @return the matching student, or null if not found
     */
    public Student findByStudentNumber(final String studentNumber) {
        return studentDAO.findByStudentNumber(studentNumber);
    }

    /**
     * Saves a new student record.
     *
     * @param student the student to save
     */
    public void save(final Student student) {
        studentDAO.save(student);
    }

    /**
     * Updates an existing student record.
     *
     * @param student the student to update
     */
    public void update(final Student student) {
        studentDAO.update(student);
    }

    /**
     * Deletes a student if no grades or course enrollments
     * prevent the deletion.
     *
     * @param studentId the identifier of the student to delete
     * @throws IllegalStateException if the student has grades
     *         or is enrolled in courses
     */
    public void delete(final Integer studentId) {
        List<Enrollment> enrollments =
                enrollmentDAO.findByStudentId(studentId);

        if (!enrollments.isEmpty()) {
            for (Enrollment enrollment : enrollments) {
                if (!gradesDAO.findByEnrollment(
                        enrollment.getId()).isEmpty()) {
                    throw new IllegalStateException(
                            "Cannot delete student: grades exist."
                    );
                }
            }

            for (Enrollment enrollment : enrollments) {
                if (enrollment.getCourseId() != null) {
                    throw new IllegalStateException(
                            "Cannot delete student: enrolled in courses."
                    );
                }
            }
        }

        studentDAO.delete(studentId);
    }

    /**
     * Finds a student and converts the result into a DTO.
     *
     * @param studentId the student identifier
     * @return the student DTO, or null if the student does not exist
     */
    public StudentDTO findDTOById(final Integer studentId) {
        Student student = studentDAO.findById(studentId);

        if (student == null) {
            return null;
        }

        User user = userDAO.findById(student.getUserId());
        return studentMapper.toDTO(user, student);
    }

    /**
     * Retrieves all students and converts them into DTOs.
     *
     * @return an unmodifiable list of student DTOs
     */
    public List<StudentDTO> findAllDTO() {
        return studentDAO.findAll()
                .stream()
                .map(student -> {
                    User user = userDAO.findById(student.getUserId());
                    return studentMapper.toDTO(user, student);
                })
                .toList();
    }

    /**
     * Creates a user and student record with a unique student number.
     *
     * @param firstName the student's first name
     * @param lastName the student's last name
     * @param phone the student's phone number
     * @return the created student as a DTO
     */
    public StudentDTO createStudent(
            final String firstName,
            final String lastName,
            final String phone) {

        User user = userService.createStudentUser(
                firstName,
                lastName,
                phone);

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

    /**
     * Updates a student and the associated user information
     * using the supplied DTO.
     *
     * @param dto the student data transfer object
     */
    public void updateFromDTO(final StudentDTO dto) {
        Student student = studentDAO.findById(dto.getStudentId());

        if (student == null) {
            return;
        }

        student.setStudentNumber(dto.getStudentNumber());
        student.setAcademicGroupId(dto.getAcademicGroupId());

        studentDAO.update(student);

        User user = userDAO.findById(dto.getUserId());

        if (user != null) {
            user.setUsername(dto.getUsername());
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setPhone(dto.getPhone());
            userDAO.update(user);
        }
    }

    /**
     * Generates a student number that does not already exist.
     *
     * @return a unique student number
     */
    public String generateUniqueStudentNumber() {
        String number;

        do {
            number = GeneratorUtil.generateStudentNumber();
        } while (studentDAO.findByStudentNumber(number) != null);

        return number;
    }
}
