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
}