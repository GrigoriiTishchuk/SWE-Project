package com.edujournal.backend.service;

import com.edujournal.dao.CourseDAO;
import com.edujournal.dao.GradesDAO;
import com.edujournal.entity.AssessmentType;
import com.edujournal.entity.Assessments;
import com.edujournal.entity.Course;
import com.edujournal.entity.Enrollment;
import com.edujournal.entity.Grades;
import com.edujournal.entity.Role;
import com.edujournal.entity.Student;
import com.edujournal.entity.User;
import com.edujournal.dao.UserDAO;
import com.edujournal.model.GradeDistributionDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DashboardStatisticsServiceTest {

    private final DashboardStatisticsService service = new DashboardStatisticsService();
    private final CourseDAO courseDAO = new CourseDAO();
    private final AssessmentsService assessmentsService = new AssessmentsService();
    private final EnrollmentService enrollmentService = new EnrollmentService();
    private final GradesDAO gradesDAO = new GradesDAO();
    private final StudentService studentService = new StudentService();
    private final UserDAO userDAO = new UserDAO();

    private Integer createdCourseId = null;
    private Integer createdAssessmentId = null;
    private Integer createdEnrollmentId = null;
    private Integer createdGradeId = null;
    private Integer createdStudentId = null;
    private User createdUser = null;

    @AfterEach
    void cleanup() {
        if (createdGradeId != null) {
            gradesDAO.deleteById(createdGradeId);
            createdGradeId = null;
        }

        if (createdEnrollmentId != null) {
            enrollmentService.delete(createdEnrollmentId);
            createdEnrollmentId = null;
        }

        if (createdAssessmentId != null) {
            Assessments a = assessmentsService.findById(createdAssessmentId);
            if (a != null) assessmentsService.delete(a);
            createdAssessmentId = null;
        }

        if (createdCourseId != null) {
            try { courseDAO.delete(createdCourseId); } catch (Exception ignored) {}
            createdCourseId = null;
        }

        if (createdStudentId != null) {
            try { studentService.delete(createdStudentId); } catch (Exception ignored) {}
            createdStudentId = null;
        }

        if (createdUser != null) {
            userDAO.delete(createdUser);
            createdUser = null;
        }
    }

    @Test
    void getAdministratorGradesReturnsList() {
        List<Integer> grades = service.getAdministratorGrades();
        assertNotNull(grades);
    }

    @Test
    void getGradeDistributionForAdministratorReturnsDto() {
        GradeDistributionDTO dto = service.getGradeDistribution(Role.ADMINISTRATOR, null);
        assertNotNull(dto);
    }

    @Test
    void getTeacherGradesReturnsEmptyForUnknownTeacher() {
        List<Integer> grades = service.getTeacherGrades(999999);
        assertNotNull(grades);
        assertTrue(grades.isEmpty());
    }

    @Test
    void getStudentGradesReturnsEmptyForNoEnrollments() {
        List<Integer> grades = service.getStudentGrades(999999);
        assertNotNull(grades);
        assertTrue(grades.isEmpty());
    }

    @Test
    void getGradeDistributionForTeacherReturnsDto() {
        GradeDistributionDTO dto = service.getGradeDistribution(Role.TEACHER, 999999);
        assertNotNull(dto);
    }

    @Test
    void administratorGradesAreInValidRange() {
        List<Integer> grades = service.getAdministratorGrades();
        for (Integer grade : grades) {
            assertTrue(grade >= 0 && grade <= 5, "Grade out of range: " + grade);
        }
    }

    @Test
    void getGradeDistributionForStudentRole() {
        User user = new User();
        user.setUsername("dash_stat_student_user");
        user.setPasswordHash("test");
        user.setFirstName("Stat");
        user.setLastName("Student");
        user.setRole(Role.STUDENT);
        userDAO.save(user);
        createdUser = user;

        Student student = new Student();
        student.setStudentNumber("DASH_STAT_001");
        student.setUserId(user.getId());
        studentService.save(student);
        createdStudentId = student.getId();

        GradeDistributionDTO dto = service.getGradeDistribution(Role.STUDENT, user.getId());
        assertNotNull(dto);
    }

    @Test
    void getAdministratorGradesProcessesCompletedCourseGrades() {
        Course course = new Course();
        course.setCode("DASH_TEST_001");
        course.setName("Dashboard Test Course");
        course.setStartDate(LocalDate.of(2024, 9, 1));
        course.setEndDate(LocalDate.of(2024, 12, 31));
        courseDAO.save(course);
        createdCourseId = course.getId();

        Assessments assessment = new Assessments();
        assessment.setCourseId(course.getId());
        assessment.setTitle("Dash Test Exam");
        assessment.setType(AssessmentType.EXAM1);
        assessment.setMaxScore(100.0);
        assessment.setWeight(100.0);
        assessmentsService.save(assessment);
        createdAssessmentId = assessment.getId();

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(1);
        enrollment.setCourseId(course.getId());
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ENROLLED");
        enrollmentService.save(enrollment);
        createdEnrollmentId = enrollment.getId();

        Grades grade = new Grades();
        grade.setEnrollmentId(enrollment.getId());
        grade.setAssessmentId(assessment.getId());
        grade.setScore(90.0);
        gradesDAO.save(grade);
        createdGradeId = grade.getId();

        List<Integer> grades = service.getAdministratorGrades();
        assertNotNull(grades);

        GradeDistributionDTO dto = service.getGradeDistribution(Role.ADMINISTRATOR, null);
        assertNotNull(dto);
    }

    @Test
    void getTeacherGradesLoopBodyExecuted() {
        User teacher = new User();
        teacher.setUsername("dash_teacher_grades_user");
        teacher.setPasswordHash("test");
        teacher.setFirstName("Dash");
        teacher.setLastName("Teacher");
        teacher.setRole(Role.TEACHER);
        userDAO.save(teacher);
        createdUser = teacher;

        Course course = new Course();
        course.setCode("DASH_TCH_001");
        course.setName("Dash Teacher Course");
        course.setStartDate(LocalDate.of(2024, 9, 1));
        course.setEndDate(LocalDate.of(2024, 12, 31));
        course.setUserId(teacher.getId());
        courseDAO.save(course);
        createdCourseId = course.getId();

        Assessments assessment = new Assessments();
        assessment.setCourseId(course.getId());
        assessment.setTitle("Dash Teacher Exam");
        assessment.setType(AssessmentType.EXAM1);
        assessment.setMaxScore(100.0);
        assessment.setWeight(100.0);
        assessmentsService.save(assessment);
        createdAssessmentId = assessment.getId();

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(1);
        enrollment.setCourseId(course.getId());
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ENROLLED");
        enrollmentService.save(enrollment);
        createdEnrollmentId = enrollment.getId();

        Grades grade = new Grades();
        grade.setEnrollmentId(enrollment.getId());
        grade.setAssessmentId(assessment.getId());
        grade.setScore(90.0);
        gradesDAO.save(grade);
        createdGradeId = grade.getId();

        List<Integer> result = service.getTeacherGrades(teacher.getId());
        assertNotNull(result);

        GradeDistributionDTO dto = service.getGradeDistribution(Role.TEACHER, teacher.getId());
        assertNotNull(dto);
    }

    @Test
    void getStudentGradesLoopBodyExecuted() {
        Student student = new Student();
        student.setStudentNumber("DASH_STU_GRADES_001");
        studentService.save(student);
        createdStudentId = student.getId();

        Course course = new Course();
        course.setCode("DASH_STU_001");
        course.setName("Dash Student Course");
        course.setStartDate(LocalDate.of(2024, 9, 1));
        course.setEndDate(LocalDate.of(2024, 12, 31));
        courseDAO.save(course);
        createdCourseId = course.getId();

        Assessments assessment = new Assessments();
        assessment.setCourseId(course.getId());
        assessment.setTitle("Dash Student Exam");
        assessment.setType(AssessmentType.EXAM1);
        assessment.setMaxScore(100.0);
        assessment.setWeight(100.0);
        assessmentsService.save(assessment);
        createdAssessmentId = assessment.getId();

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(student.getId());
        enrollment.setCourseId(course.getId());
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ENROLLED");
        enrollmentService.save(enrollment);
        createdEnrollmentId = enrollment.getId();

        Grades grade = new Grades();
        grade.setEnrollmentId(enrollment.getId());
        grade.setAssessmentId(assessment.getId());
        grade.setScore(85.0);
        gradesDAO.save(grade);
        createdGradeId = grade.getId();

        List<Integer> result = service.getStudentGrades(student.getId());
        assertNotNull(result);
    }

    @Test
    void getStudentGradesSkipsWhenNoAssessments() {
        Student student = new Student();
        student.setStudentNumber("DASH_STU_NOASSESS_001");
        studentService.save(student);
        createdStudentId = student.getId();

        Course course = new Course();
        course.setCode("DASH_STU_NA_001");
        course.setName("Dash No Assess Course");
        courseDAO.save(course);
        createdCourseId = course.getId();

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(student.getId());
        enrollment.setCourseId(course.getId());
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ENROLLED");
        enrollmentService.save(enrollment);
        createdEnrollmentId = enrollment.getId();

        List<Integer> result = service.getStudentGrades(student.getId());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getStudentGradesSkipsWhenNoGrades() {
        Student student = new Student();
        student.setStudentNumber("DASH_STU_NOGRADE_001");
        studentService.save(student);
        createdStudentId = student.getId();

        Course course = new Course();
        course.setCode("DASH_STU_NG_001");
        course.setName("Dash No Grade Course");
        courseDAO.save(course);
        createdCourseId = course.getId();

        Assessments assessment = new Assessments();
        assessment.setCourseId(course.getId());
        assessment.setTitle("Dash No Grade Exam");
        assessment.setType(AssessmentType.EXAM1);
        assessment.setMaxScore(100.0);
        assessment.setWeight(100.0);
        assessmentsService.save(assessment);
        createdAssessmentId = assessment.getId();

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(student.getId());
        enrollment.setCourseId(course.getId());
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ENROLLED");
        enrollmentService.save(enrollment);
        createdEnrollmentId = enrollment.getId();

        List<Integer> result = service.getStudentGrades(student.getId());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getStudentGradesSkipsPartialGrades() {
        Student student = new Student();
        student.setStudentNumber("DASH_STU_PART_001");
        studentService.save(student);
        createdStudentId = student.getId();

        Course course = new Course();
        course.setCode("DASH_STU_PR_001");
        course.setName("Dash Partial Grade Course");
        courseDAO.save(course);
        createdCourseId = course.getId();

        Assessments assessment1 = new Assessments();
        assessment1.setCourseId(course.getId());
        assessment1.setTitle("Dash Partial Exam 1");
        assessment1.setType(AssessmentType.EXAM1);
        assessment1.setMaxScore(100.0);
        assessment1.setWeight(50.0);
        assessmentsService.save(assessment1);
        createdAssessmentId = assessment1.getId();

        Assessments assessment2 = new Assessments();
        assessment2.setCourseId(course.getId());
        assessment2.setTitle("Dash Partial Exam 2");
        assessment2.setType(AssessmentType.EXAM2);
        assessment2.setMaxScore(100.0);
        assessment2.setWeight(50.0);
        assessmentsService.save(assessment2);

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(student.getId());
        enrollment.setCourseId(course.getId());
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ENROLLED");
        enrollmentService.save(enrollment);
        createdEnrollmentId = enrollment.getId();

        Grades grade = new Grades();
        grade.setEnrollmentId(enrollment.getId());
        grade.setAssessmentId(assessment1.getId());
        grade.setScore(90.0);
        gradesDAO.save(grade);
        createdGradeId = grade.getId();

        try {
            List<Integer> result = service.getStudentGrades(student.getId());
            assertNotNull(result);
            assertTrue(result.isEmpty());
        } finally {
            assessmentsService.delete(assessment2);
        }
    }

    @Test
    void getTeacherGradesSkipsNullEndDate() {
        User teacher = new User();
        teacher.setUsername("dash_tch_null_end_user");
        teacher.setPasswordHash("test");
        teacher.setFirstName("Dash");
        teacher.setLastName("NullEnd");
        teacher.setRole(Role.TEACHER);
        userDAO.save(teacher);
        createdUser = teacher;

        Course course = new Course();
        course.setCode("DASH_TCH_NE_001");
        course.setName("Dash Null End Course");
        course.setUserId(teacher.getId());
        // endDate intentionally not set
        courseDAO.save(course);
        createdCourseId = course.getId();

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(1);
        enrollment.setCourseId(course.getId());
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ENROLLED");
        enrollmentService.save(enrollment);
        createdEnrollmentId = enrollment.getId();

        List<Integer> result = service.getTeacherGrades(teacher.getId());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getTeacherGradesSkipsFutureCourse() {
        User teacher = new User();
        teacher.setUsername("dash_tch_future_user");
        teacher.setPasswordHash("test");
        teacher.setFirstName("Dash");
        teacher.setLastName("Future");
        teacher.setRole(Role.TEACHER);
        userDAO.save(teacher);
        createdUser = teacher;

        Course course = new Course();
        course.setCode("DASH_TCH_FT_001");
        course.setName("Dash Future Course");
        course.setStartDate(LocalDate.of(2026, 9, 1));
        course.setEndDate(LocalDate.of(2030, 6, 30));
        course.setUserId(teacher.getId());
        courseDAO.save(course);
        createdCourseId = course.getId();

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(1);
        enrollment.setCourseId(course.getId());
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ENROLLED");
        enrollmentService.save(enrollment);
        createdEnrollmentId = enrollment.getId();

        List<Integer> result = service.getTeacherGrades(teacher.getId());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
