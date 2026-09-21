package com.edujournal.dao;

import com.edujournal.entity.Course;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CourseDAOTest {

    private final CourseDAO courseDAO = new CourseDAO();

    private final List<Integer> createdIds = new ArrayList<>();

    @AfterEach
    void cleanup() {
        for (Integer id : createdIds) {
            courseDAO.delete(id);
        }

        createdIds.clear();
    }

    @Test
    void testFindAll() {

        List<Course> courses = courseDAO.findAll();

        assertNotNull(courses);

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

        assertFalse(courses.isEmpty());
    }

    @Test
    void testFindByName() {

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
    void testFindByGroupId() {

        List<Course> courses =
                courseDAO.findByGroupId(3);

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
            );
        }
    }

    @Test
    void testFindByCode() {

        Course course =
                courseDAO.findByCode("SE01");

        assertNotNull(course);
        assertEquals("SE01", course.getCode());
        assertEquals(
                "Software Engineering",
                course.getName()
        );
    }

    @Test
    void testExistsByCode() {

        boolean exists =
                courseDAO.existsByCode("SE01");

        assertTrue(exists);
    }

    @Test
    void testFindByUserId() {

        List<Course> courses =
                courseDAO.findByUserId(1);

        assertNotNull(courses);

        for (Course course : courses) {
            System.out.println(
                    course.getId()
                            + " - "
                            + course.getCode()
                            + " - "
                            + course.getName()
            );
        }
    }

    @Test
    void testSaveAndFindById() {

        Course course = new Course();

        course.setCode("TEST01");
        course.setName("Test Course");

        course.setAcademicGroupId(3);

        courseDAO.save(course);

        createdIds.add(course.getId());

        assertNotNull(course.getId());

        Course found =
                courseDAO.findById(course.getId());

        assertNotNull(found);
        assertEquals("TEST01", found.getCode());
        assertEquals("Test Course", found.getName());
    }

    @Test
    void testUpdate() {

        Course course = new Course();

        course.setCode("TEST02");
        course.setName("Old Course");

        course.setAcademicGroupId(3);

        courseDAO.save(course);

        createdIds.add(course.getId());

        course.setName("Updated Course");

        courseDAO.update(course);

        Course updated =
                courseDAO.findById(course.getId());

        assertNotNull(updated);
        assertEquals(
                "Updated Course",
                updated.getName()
        );
    }

    @Test
    void testDelete() {

        Course course = new Course();

        course.setCode("DELETE01");
        course.setName("Course To Delete");

        course.setAcademicGroupId(3);

        courseDAO.save(course);

        Integer id = course.getId();

        assertNotNull(
                courseDAO.findById(id)
        );

        courseDAO.delete(id);

        assertNull(
                courseDAO.findById(id)
        );
    }
}