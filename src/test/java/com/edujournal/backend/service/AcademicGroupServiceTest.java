package com.edujournal.backend.service;

import com.edujournal.entity.AcademicGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AcademicGroupServiceTest {

    private final AcademicGroupService academicGroupService =
            new AcademicGroupService();

    private final List<Integer> createdIds =
            new ArrayList<>();

    @AfterEach
    void cleanup() {
        for (Integer id : createdIds) {
            academicGroupService.delete(id);
        }

        createdIds.clear();
    }

    @Test
    void saveAndFindById() {
        AcademicGroup group = new AcademicGroup();
        group.setName("Service Test Group 1");

        academicGroupService.save(group);

        createdIds.add(group.getId());

        assertNotNull(group.getId());

        AcademicGroup found =
                academicGroupService.findById(group.getId());

        assertNotNull(found);
        assertEquals(
                "Service Test Group 1",
                found.getName()
        );
    }

    @Test
    void findAll() {
        List<AcademicGroup> groups =
                academicGroupService.findAll();

        assertNotNull(groups);
    }

    @Test
    void findByName() {
        AcademicGroup group = new AcademicGroup();
        group.setName("Service Test Group 2");

        academicGroupService.save(group);

        createdIds.add(group.getId());

        AcademicGroup found =
                academicGroupService.findByName(
                        "Service Test Group 2"
                );

        assertNotNull(found);
        assertEquals(
                "Service Test Group 2",
                found.getName()
        );
    }

    @Test
    void update() {
        AcademicGroup group = new AcademicGroup();
        group.setName("Old Service Group");

        academicGroupService.save(group);

        createdIds.add(group.getId());

        group.setName("New Service Group");

        academicGroupService.update(group);

        AcademicGroup updated =
                academicGroupService.findById(group.getId());

        assertNotNull(updated);
        assertEquals(
                "New Service Group",
                updated.getName()
        );
    }

    @Test
    void delete() {
        AcademicGroup group = new AcademicGroup();
        group.setName("Service Group To Delete");

        academicGroupService.save(group);

        Integer id = group.getId();

        assertNotNull(
                academicGroupService.findById(id)
        );

        academicGroupService.delete(id);

        assertNull(
                academicGroupService.findById(id)
        );
    }
}