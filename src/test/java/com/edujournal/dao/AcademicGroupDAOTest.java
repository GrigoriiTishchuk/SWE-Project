package com.edujournal.dao;

import com.edujournal.entity.AcademicGroup;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AcademicGroupDAOTest {

    private final AcademicGroupDAO academicGroupDAO =
            new AcademicGroupDAO();

    @Test
    void saveAndFindById() {
        AcademicGroup group = new AcademicGroup();
        group.setName("Test Group 1");

        academicGroupDAO.save(group);

        assertNotNull(group.getId());

        AcademicGroup found =
                academicGroupDAO.findById(group.getId());

        assertNotNull(found);
        assertEquals("Test Group 1", found.getName());
    }

    @Test
    void findAll() {
        List<AcademicGroup> groups =
                academicGroupDAO.findAll();

        assertNotNull(groups);
    }

    @Test
    void findByName() {
        AcademicGroup group = new AcademicGroup();
        group.setName("Test Group 2");

        academicGroupDAO.save(group);

        AcademicGroup found =
                academicGroupDAO.findByName("Test Group 2");

        assertNotNull(found);
        assertEquals("Test Group 2", found.getName());
    }

    @Test
    void update() {
        AcademicGroup group = new AcademicGroup();
        group.setName("Old Group Name");

        academicGroupDAO.save(group);

        group.setName("New Group Name");

        academicGroupDAO.update(group);

        AcademicGroup updated =
                academicGroupDAO.findById(group.getId());

        assertNotNull(updated);
        assertEquals("New Group Name", updated.getName());
    }

    @Test
    void delete() {
        AcademicGroup group = new AcademicGroup();
        group.setName("Group To Delete");

        academicGroupDAO.save(group);

        Integer id = group.getId();

        assertNotNull(academicGroupDAO.findById(id));

        academicGroupDAO.delete(id);

        assertNull(academicGroupDAO.findById(id));
    }
}