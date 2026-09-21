package com.edujournal.backend.utils;

import com.edujournal.dao.AcademicGroupDAO;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.entity.Course;
import com.edujournal.entity.User;
import com.edujournal.model.CourseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CourseMapperTest {

    @Test
    void toDTOTest() {
    }

    @Test
    void toEntity_TeacherNameTest() {
        UserDAO userDAO = mock(UserDAO.class);
        AcademicGroupDAO groupDAO = mock(AcademicGroupDAO.class);

        User teacher = new User();
        teacher.setId(10);
        teacher.setFirstName("Matti");
        teacher.setLastName("Matala");

        when(userDAO.findById(10)).thenReturn(teacher);

        CourseMapper mapper = new CourseMapper(userDAO, groupDAO);

        Course course = new Course();
        course.setId(1);
        course.setCode("PR01");
        course.setName("Programming");
        course.setUserId(10);

        CourseDTO dto = mapper.toDTO(course);

        assertEquals("Matti Matala", dto.getTeacherName());
    }

    @Test
    void toDTO_GroupNameTest() {
        UserDAO userDAO = mock(UserDAO.class);
        AcademicGroupDAO groupDAO = mock(AcademicGroupDAO.class);

        AcademicGroup group = new AcademicGroup();
        group.setId(3);
        group.setName("Group A");

        when(groupDAO.findById(3)).thenReturn(group);

        CourseMapper mapper = new CourseMapper(userDAO, groupDAO);

        Course course = new Course();
        course.setId(1);
        course.setCode("MA01");
        course.setName("Math");
        course.setAcademicGroupId(3);

        CourseDTO dto = mapper.toDTO(course);

        assertEquals("Group A", dto.getGroupName());
    }

    @Test
    void toDTO_NoTeacherTest() {
        UserDAO userDAO = mock(UserDAO.class);
        AcademicGroupDAO groupDAO = mock(AcademicGroupDAO.class);

        CourseMapper mapper = new CourseMapper(userDAO, groupDAO);

        Course course = new Course();
        course.setId(1);
        course.setCode("PR01");
        course.setName("Programming");
        course.setUserId(null);

        CourseDTO dto = mapper.toDTO(course);

        assertNull(dto.getTeacherName());
    }

    @Test
    void toDTO_NoGroupTest() {
        UserDAO userDAO = mock(UserDAO.class);
        AcademicGroupDAO groupDAO = mock(AcademicGroupDAO.class);

        CourseMapper mapper = new CourseMapper(userDAO, groupDAO);

        Course course = new Course();
        course.setId(1);
        course.setCode("PR01");
        course.setName("Programming");
        course.setAcademicGroupId(null);

        CourseDTO dto = mapper.toDTO(course);

        assertNull(dto.getGroupName());
    }

    @Test
    void toEntityTest() {
        CourseMapper mapper = new CourseMapper(null, null);

        CourseDTO dto = new CourseDTO(
                1,
                "PR01",
                "Programming",
                10,
                "Matti Matala",
                3,
                "Group A"
        );

        Course entity = mapper.toEntity(dto);

        assertEquals(1, entity.getId());
        assertEquals("PR01", entity.getCode());
        assertEquals("Programming", entity.getName());
        assertEquals(10, entity.getUserId());
        assertEquals(3, entity.getAcademicGroupId());
    }

}