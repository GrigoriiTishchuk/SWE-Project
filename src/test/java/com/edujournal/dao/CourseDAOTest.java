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
}