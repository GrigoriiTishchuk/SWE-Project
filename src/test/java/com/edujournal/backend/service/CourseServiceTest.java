package com.edujournal.backend.service;

import com.edujournal.dao.UserDAO;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.entity.Course;
import com.edujournal.entity.Enrollment;
import com.edujournal.entity.Role;
import com.edujournal.entity.Student;
import com.edujournal.entity.User;
import com.edujournal.model.CourseDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseServiceTest {

    private final CourseService courseService = new CourseService();
    private final UserDAO userDAO = new UserDAO();
    private final AcademicGroupService academicGroupService = new AcademicGroupService();
    private final List<Integer> createdGroupIds = new ArrayList<>();

    private final List<Integer> createdCourseIds = new ArrayList<>();
    private final List<User> createdUsers = new ArrayList<>();

    @AfterEach
    void cleanup() {
        for (Integer id : createdCourseIds) {
            try {
                courseService.delete(id);
            } catch (Exception ignored) {
            }
        }

        for (User user : createdUsers) {
            userDAO.delete(user);
        }

        createdCourseIds.clear();
        createdUsers.clear();

        for (Integer id : createdGroupIds) {
            try {
                academicGroupService.delete(id);
            } catch (Exception ignored) {
            }
        }

        createdGroupIds.clear();
    }

    private Course buildCourse(String code, String name) {
        Course course = new Course();
        course.setCode(code);
        course.setName(name);
        course.setStartDate(LocalDate.of(2026, 9, 1));
        course.setEndDate(LocalDate.of(2027, 6, 30));
        return course;
    }

    @Test
    void saveAndFindByCode() {
        Course course = buildCourse("SVC_TEST_001", "Service Test Course");

        courseService.save(course);
        createdCourseIds.add(course.getId());

        assertNotNull(course.getId());

        CourseDTO found = courseService.findByCode("SVC_TEST_001");

        assertNotNull(found);
        assertEquals("SVC_TEST_001", found.getCode());
        assertEquals("Service Test Course", found.getName());
    }

    @Test
    void saveThrowsForDuplicateCode() {
        Course first = buildCourse("SVC_TEST_DUP", "First Course");
        courseService.save(first);
        createdCourseIds.add(first.getId());

        Course second = buildCourse("SVC_TEST_DUP", "Second Course");

        assertThrows(
                IllegalArgumentException.class,
                () -> courseService.save(second)
        );
    }

    @Test
    void findAll() {
        List<CourseDTO> courses = courseService.findAll();
        assertNotNull(courses);
    }

    @Test
    void existsByCode() {
        Course course = buildCourse("SVC_TEST_EXISTS", "Exists Course");
        courseService.save(course);
        createdCourseIds.add(course.getId());

        assertTrue(courseService.existsByCode("SVC_TEST_EXISTS"));
        assertFalse(courseService.existsByCode("SVC_TEST_NONEXISTENT_XYZ"));
    }

    @Test
    void getById() {
        Course course = buildCourse("SVC_TEST_BYID", "By ID Course");
        courseService.save(course);
        createdCourseIds.add(course.getId());

        CourseDTO found = courseService.getById(course.getId());

        assertNotNull(found);
        assertEquals(course.getId(), found.getId());
        assertEquals("SVC_TEST_BYID", found.getCode());
    }

    @Test
    void getByIdReturnsNullForNonExistent() {
        assertNull(courseService.getById(999999));
    }

    @Test
    void delete() {
        Course course = buildCourse("SVC_TEST_DEL", "Delete Course");
        courseService.save(course);
        Integer id = course.getId();

        assertNotNull(courseService.getById(id));

        courseService.delete(id);

        assertNull(courseService.getById(id));
    }

    @Test
    void assignTeacherToCourse() {
        User teacher = new User();
        teacher.setUsername("svc_test_teacher");
        teacher.setPasswordHash("test");
        teacher.setFirstName("Svc");
        teacher.setLastName("Teacher");
        teacher.setRole(Role.TEACHER);

        userDAO.save(teacher);
        createdUsers.add(teacher);

        Course course = buildCourse("SVC_TEST_TCHR", "Teacher Assign Course");
        courseService.save(course);
        createdCourseIds.add(course.getId());

        assertDoesNotThrow(
                () -> courseService.assignTeacherToCourse(course.getId(), teacher.getId())
        );

        CourseDTO updated = courseService.getById(course.getId());
        assertEquals(teacher.getId(), updated.getUserId());
    }

    @Test
    void assignTeacherToCourseThrowsForNonTeacherRole() {
        User student = new User();
        student.setUsername("svc_test_student_role");
        student.setPasswordHash("test");
        student.setFirstName("Svc");
        student.setLastName("Student");
        student.setRole(Role.STUDENT);

        userDAO.save(student);
        createdUsers.add(student);

        Course course = buildCourse("SVC_TEST_BADROL", "Bad Role Course");
        courseService.save(course);
        createdCourseIds.add(course.getId());

        assertThrows(
                IllegalArgumentException.class,
                () -> courseService.assignTeacherToCourse(course.getId(), student.getId())
        );
    }

    @Test
    void assignTeacherToCourseThrowsForNonExistentCourse() {
        User teacher = new User();
        teacher.setUsername("svc_test_teacher2");
        teacher.setPasswordHash("test");
        teacher.setFirstName("Svc2");
        teacher.setLastName("Teacher2");
        teacher.setRole(Role.TEACHER);

        userDAO.save(teacher);
        createdUsers.add(teacher);

        assertThrows(
                IllegalArgumentException.class,
                () -> courseService.assignTeacherToCourse(999999, teacher.getId())
        );
    }

    @Test
    void findByUserId() {
        User teacher = new User();
        teacher.setUsername("svc_test_teacher3");
        teacher.setPasswordHash("test");
        teacher.setFirstName("Svc3");
        teacher.setLastName("Teacher3");
        teacher.setRole(Role.TEACHER);

        userDAO.save(teacher);
        createdUsers.add(teacher);

        Course course = buildCourse("SVC_TEST_BYUID", "By User Course");
        courseService.save(course);
        createdCourseIds.add(course.getId());

        courseService.assignTeacherToCourse(course.getId(), teacher.getId());

        List<CourseDTO> result = courseService.findByUserId(teacher.getId());

        assertNotNull(result);
        assertTrue(
                result.stream().anyMatch(c -> c.getId().equals(course.getId()))
        );
    }

    @Test
    void findByName() {
        Course course = buildCourse("SVC_TEST_NAME", "Unique Named Course");
        courseService.save(course);
        createdCourseIds.add(course.getId());

        CourseDTO found = courseService.findByName("Unique Named Course");

        assertNotNull(found);
        assertEquals("SVC_TEST_NAME", found.getCode());
    }

    @Test
    void update() {
        Course course = buildCourse("SVC_TEST_UPD", "Original Course Name");
        courseService.save(course);
        createdCourseIds.add(course.getId());

        course.setName("Updated Course Name");
        courseService.update(course);

        CourseDTO updated = courseService.getById(course.getId());
        assertNotNull(updated);
        assertEquals("Updated Course Name", updated.getName());
    }

    @Test
    void findByGroupId() {
        AcademicGroup group = new AcademicGroup();
        group.setName("SVC Course Group Test");
        academicGroupService.save(group);
        createdGroupIds.add(group.getId());

        Course course = buildCourse("SVC_TEST_BYGRP", "By Group Course");
        course.setAcademicGroupId(group.getId());
        courseService.save(course);
        createdCourseIds.add(course.getId());

        List<CourseDTO> result = courseService.findByGroupId(group.getId());

        assertNotNull(result);
        assertTrue(result.stream().anyMatch(c -> c.getId().equals(course.getId())));
    }

    @Test
    void assignStudentsToEnrollments() {
        StudentService studentService = new StudentService();
        EnrollmentService enrollmentService = new EnrollmentService();

        AcademicGroup group = new AcademicGroup();
        group.setName("SVC Enroll Group Test");
        academicGroupService.save(group);
        createdGroupIds.add(group.getId());

        Student student = new Student();
        student.setStudentNumber("SVC_ENRL_STU_001");
        studentService.save(student);

        academicGroupService.addStudentToGroup(student.getId(), group.getId());

        Course course = buildCourse("SVC_TEST_ENRL", "Enroll Course");
        courseService.save(course);
        createdCourseIds.add(course.getId());

        courseService.assignStudentsToEnrollments(course.getId(), group.getId());

        Enrollment enrollment = enrollmentService.findByStudentAndCourse(student.getId(), course.getId());
        assertNotNull(enrollment);

        academicGroupService.removeStudentFromGroup(student.getId(), group.getId());
        studentService.delete(student.getId());
    }
}
