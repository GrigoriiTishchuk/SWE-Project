package com.edujournal.dao;

import com.edujournal.entity.Course;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CourseDAOTest {

    @Test
    void testFindAll() {

        CourseDAO courseDAO = new CourseDAO();

        List<Course> courses = courseDAO.findAll();

        System.out.println("Course count: " + courses.size());

        for (Course course : courses) {
            System.out.println(
                    course.getId()
                            + " - "
                            + course.getCode()
                            + " - "
                            + course.getName()
            );
        }

        assertNotNull(courses);
        assertFalse(courses.isEmpty());
    }

    @Test
    void testFindByName() {

        CourseDAO courseDAO = new CourseDAO();

        Course course =
                courseDAO.findByName("Software Engineering");

        assertNotNull(course);

        System.out.println(
                "Found course: "
                        + course.getId()
                        + " - "
                        + course.getName()
        );

        assertEquals(
                "SE01",
                course.getCode()
        );
    }

    @Test
    void testFindByAcademicGroupId() {

        CourseDAO courseDAO = new CourseDAO();

        List<Course> courses =
                courseDAO.findByAcademicGroupId(3);

        assertNotNull(courses);
        assertEquals(2, courses.size());

        assertTrue(
                courses.stream()
                        .anyMatch(course ->
                                "PR01".equals(course.getCode())
                        )
        );

        assertTrue(
                courses.stream()
                        .anyMatch(course ->
                                "MA01".equals(course.getCode())
                        )
        );

        for (Course course : courses) {
            System.out.println(
                    course.getCode()
                            + " - "
                            + course.getName()
                            + " - Group: "
                            + course.getAcademicGroup().getName()
            );

            assertNotNull(course.getAcademicGroup());
            assertEquals(3, course.getAcademicGroup().getId());
        }
    }
}