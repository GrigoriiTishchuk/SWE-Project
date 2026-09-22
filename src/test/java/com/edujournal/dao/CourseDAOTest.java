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
        assertFalse(courses.isEmpty());
    }

    @Test
    void testFindByName() {

        Course course =
                courseDAO.findByName("Software Engineering");

        assertNotNull(course);

        assertEquals(
                "SE01",
                course.getCode()
        );
    }
}