package com.edujournal.backend.service;

import com.edujournal.entity.AcademicGroup;
import com.edujournal.model.AcademicGroupDTO;
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
    void findByIdReturnsNullForNonExistingId() {
        AcademicGroup found =
                academicGroupService.findById(4423);

        assertNull(found);
    }

    @Test
    void findAllReturnsSavedGroups() {
        AcademicGroup group1 = new AcademicGroup();
        group1.setName("Service Test Group A");

        AcademicGroup group2 = new AcademicGroup();
        group2.setName("Service Test Group B");

        academicGroupService.save(group1);
        academicGroupService.save(group2);

        createdIds.add(group1.getId());
        createdIds.add(group2.getId());

        List<AcademicGroup> groups =
                academicGroupService.findAll();

        assertNotNull(groups);

        assertTrue(
                groups.stream()
                        .anyMatch(g ->
                                "Service Test Group A"
                                        .equals(g.getName()))
        );

        assertTrue(
                groups.stream()
                        .anyMatch(g ->
                                "Service Test Group B"
                                        .equals(g.getName()))
        );
    }

    @Test
    void findAllWithMultipleGroups() {
        AcademicGroup group1 = new AcademicGroup();
        group1.setName("Service Group A");

        AcademicGroup group2 = new AcademicGroup();
        group2.setName("Service Group B");

        AcademicGroup group3 = new AcademicGroup();
        group3.setName("Service Group C");

        academicGroupService.save(group1);
        academicGroupService.save(group2);
        academicGroupService.save(group3);

        createdIds.add(group1.getId());
        createdIds.add(group2.getId());
        createdIds.add(group3.getId());

        List<AcademicGroup> groups =
                academicGroupService.findAll();

        assertTrue(groups.size() >= 3);
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
    void findByNameReturnsNullForNonExistingName() {
        AcademicGroup found =
                academicGroupService.findByName("Non Existing Group");

        assertNull(found);
    }

    @Test
    void findByNameWithDuplicateNames() {
        AcademicGroup group1 = new AcademicGroup();
        group1.setName("Duplicate Service Group");

        AcademicGroup group2 = new AcademicGroup();
        group2.setName("Duplicate Service Group");

        academicGroupService.save(group1);
        academicGroupService.save(group2);

        createdIds.add(group1.getId());
        createdIds.add(group2.getId());

        AcademicGroup found =
                academicGroupService.findByName(
                        "Duplicate Service Group"
                );

        assertNotNull(found);
        assertEquals(
                "Duplicate Service Group",
                found.getName()
        );
    }

    @Test
    void findAllDTO() {
        List<AcademicGroupDTO> result = academicGroupService.findAllDTO();

        assertNotNull(result);
    }

    @Test
    void existsByNameReturnsTrue() {
        AcademicGroup group = new AcademicGroup();
        group.setName("Existing Group");

        academicGroupService.save(group);

        boolean result =
                academicGroupService.existsByName("Existing Group");

        assertTrue(result);

        academicGroupService.delete(group.getId());
    }

    @Test
    void existsByNameReturnsFalse() {
        boolean result =
                academicGroupService.existsByName("Non Existing Group");

        assertFalse(result);
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
    void updateWithNullName() {
        AcademicGroup group = new AcademicGroup();
        group.setName("Service Group Before Update");

        academicGroupService.save(group);
        createdIds.add(group.getId());

        group.setName(null);

        assertThrows(Exception.class, () ->
                academicGroupService.update(group)
        );
    }

    @Test
    void updateWithEmptyName() {
        AcademicGroup group = new AcademicGroup();
        group.setName("Service Group Before Update");

        academicGroupService.save(group);
        createdIds.add(group.getId());

        group.setName("");

        academicGroupService.update(group);

        AcademicGroup updated =
                academicGroupService.findById(group.getId());

        assertNotNull(updated);
        assertEquals("", updated.getName());
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

    @Test
    void deleteNonExistingIdDoesNotThrowException() {
        assertDoesNotThrow(() ->
                academicGroupService.delete(4423)
        );
    }

    @Test
    void findAllAfterDelete() {
        AcademicGroup group = new AcademicGroup();
        group.setName("Service Group To Delete");

        academicGroupService.save(group);

        Integer id = group.getId();

        academicGroupService.delete(id);

        List<AcademicGroup> groups =
                academicGroupService.findAll();

        assertTrue(
                groups.stream()
                        .noneMatch(g -> id.equals(g.getId()))
        );
    }

    @Test
    void saveWithNullName() {
        AcademicGroup group = new AcademicGroup();
        group.setName(null);

        assertThrows(Exception.class, () ->
                academicGroupService.save(group)
        );
    }

    @Test
    void saveWithEmptyName() {
        AcademicGroup group = new AcademicGroup();
        group.setName("");

        academicGroupService.save(group);

        assertNotNull(group.getId());

        createdIds.add(group.getId());
    }

    @Test
    void saveWithLongName() {
        AcademicGroup group = new AcademicGroup();

        group.setName("A".repeat(101));

        assertThrows(Exception.class, () ->
                academicGroupService.save(group)
        );
    }
}