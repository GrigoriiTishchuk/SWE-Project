package com.edujournal.dao;

import com.edujournal.entity.AcademicGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AcademicGroupDAOTest {

    private final AcademicGroupDAO academicGroupDAO =
            new AcademicGroupDAO();

    private final List<Integer> createdIds = new ArrayList<>();

    @AfterEach
    void cleanup() {
        for (Integer id : createdIds) {
            academicGroupDAO.delete(id);
        }

        createdIds.clear();
    }

    @Test
    void saveAndFindById() {
        AcademicGroup group = new AcademicGroup();
        group.setName("Test Group 1");

        academicGroupDAO.save(group);
        createdIds.add(group.getId());

        assertNotNull(group.getId());

        AcademicGroup found =
                academicGroupDAO.findById(group.getId());

        assertNotNull(found);
        assertEquals("Test Group 1", found.getName());
    }

    @Test
    void findByIdReturnsNullForNonExistingId() {
        AcademicGroup found =
                academicGroupDAO.findById(444);

        assertNull(found);
    }

    @Test
    void findAllReturnsSavedGroups() {
        AcademicGroup group1 = new AcademicGroup();
        group1.setName("Test Group A");

        AcademicGroup group2 = new AcademicGroup();
        group2.setName("Test Group B");

        academicGroupDAO.save(group1);
        academicGroupDAO.save(group2);

        createdIds.add(group1.getId());
        createdIds.add(group2.getId());

        List<AcademicGroup> groups =
                academicGroupDAO.findAll();

        assertNotNull(groups);

        assertTrue(
                groups.stream()
                        .anyMatch(g -> "Test Group A".equals(g.getName()))
        );

        assertTrue(
                groups.stream()
                        .anyMatch(g -> "Test Group B".equals(g.getName()))
        );
    }

    @Test
    void findAllWithMultipleGroups() {
        AcademicGroup group1 = new AcademicGroup();
        group1.setName("Group A");

        AcademicGroup group2 = new AcademicGroup();
        group2.setName("Group B");

        AcademicGroup group3 = new AcademicGroup();
        group3.setName("Group C");

        academicGroupDAO.save(group1);
        academicGroupDAO.save(group2);
        academicGroupDAO.save(group3);

        createdIds.add(group1.getId());
        createdIds.add(group2.getId());
        createdIds.add(group3.getId());

        List<AcademicGroup> groups =
                academicGroupDAO.findAll();

        assertTrue(groups.size() >= 3);
    }

    @Test
    void findByName() {
        AcademicGroup group = new AcademicGroup();
        group.setName("Test Group 2");

        academicGroupDAO.save(group);
        createdIds.add(group.getId());

        AcademicGroup found =
                academicGroupDAO.findByName("Test Group 2");

        assertNotNull(found);
        assertEquals("Test Group 2", found.getName());
    }

    @Test
    void findByNameReturnsNullForNonExistingName() {
        AcademicGroup found =
                academicGroupDAO.findByName("Non Existing Group");

        assertNull(found);
    }

    @Test
    void findByNameWithDuplicateNames() {
        AcademicGroup group1 = new AcademicGroup();
        group1.setName("Duplicate Group");

        AcademicGroup group2 = new AcademicGroup();
        group2.setName("Duplicate Group");

        academicGroupDAO.save(group1);
        academicGroupDAO.save(group2);

        createdIds.add(group1.getId());
        createdIds.add(group2.getId());

        AcademicGroup found =
                academicGroupDAO.findByName("Duplicate Group");

        assertNotNull(found);
        assertEquals("Duplicate Group", found.getName());
    }

    @Test
    void update() {
        AcademicGroup group = new AcademicGroup();
        group.setName("Old Group Name");

        academicGroupDAO.save(group);
        createdIds.add(group.getId());

        group.setName("New Group Name");

        academicGroupDAO.update(group);

        AcademicGroup updated =
                academicGroupDAO.findById(group.getId());

        assertNotNull(updated);
        assertEquals("New Group Name", updated.getName());
    }

    @Test
    void updateWithNullEntity() {
        assertThrows(Exception.class, () ->
                academicGroupDAO.update(null)
        );
    }
    @Test
    void updateWithNullName() {
        AcademicGroup group = new AcademicGroup();
        group.setName("Group Before Update");

        academicGroupDAO.save(group);
        createdIds.add(group.getId());

        group.setName(null);

        assertThrows(Exception.class, () ->
                academicGroupDAO.update(group)
        );
    }

    @Test
    void updateWithEmptyName() {
        AcademicGroup group = new AcademicGroup();
        group.setName("Group Before Update");

        academicGroupDAO.save(group);
        createdIds.add(group.getId());

        group.setName("");

        academicGroupDAO.update(group);

        AcademicGroup updated =
                academicGroupDAO.findById(group.getId());

        assertNotNull(updated);
        assertEquals("", updated.getName());
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

        // Zaten silindiği için cleanup'ın tekrar silmeye
        // çalışmaması adına listede tutmuyoruz.
    }

    @Test
    void deleteWithNullId() {
        assertThrows(Exception.class, () ->
                academicGroupDAO.delete(null)
        );
    }

    @Test
    void deleteAndFindById() {
        AcademicGroup group = new AcademicGroup();
        group.setName("Group To Delete");

        academicGroupDAO.save(group);

        Integer id = group.getId();

        academicGroupDAO.delete(id);

        AcademicGroup found =
                academicGroupDAO.findById(id);

        assertNull(found);
    }

    @Test
    void deleteNonExistingIdDoesNotThrowException() {
        assertDoesNotThrow(() ->
                academicGroupDAO.delete(444)
        );
    }

    @Test
    void findAllAfterDelete() {
        AcademicGroup group = new AcademicGroup();
        group.setName("Group To Delete");

        academicGroupDAO.save(group);

        Integer id = group.getId();

        academicGroupDAO.delete(id);

        List<AcademicGroup> groups =
                academicGroupDAO.findAll();

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
                academicGroupDAO.save(group)
        );
    }

    @Test
    void saveWithEmptyName() {
        AcademicGroup group = new AcademicGroup();
        group.setName("");

        academicGroupDAO.save(group);

        assertNotNull(group.getId());

        createdIds.add(group.getId());
    }

    @Test
    void saveWithLongName() {
        AcademicGroup group = new AcademicGroup();

        group.setName("A".repeat(101));

        assertThrows(Exception.class, () ->
                academicGroupDAO.save(group)
        );
    }

    @Test
    void saveWithNullEntity() {
        assertThrows(Exception.class, () ->
                academicGroupDAO.save(null)
        );
    }

    @Test
    void saveWithInvalidEntity() {
        AcademicGroup group = new AcademicGroup();
        group.setName(null);

        assertThrows(Exception.class, () ->
                academicGroupDAO.save(group)
        );
    }

}