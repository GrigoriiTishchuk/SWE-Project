package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import com.edujournal.entity.Course;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CourseDAOTest {

    private final CourseDAO courseDAO = new CourseDAO();

    private final List<Integer> createdIds = new ArrayList<>();

    @AfterEach
    void cleanup() {
        for (Integer id : createdIds) {
            try { courseDAO.delete(id); } catch (Exception ignored) {}
        }
        createdIds.clear();
    }

    private Course buildCourse(String code, String name) {
        Course c = new Course();
        c.setCode(code);
        c.setName(name);
        c.setStartDate(LocalDate.of(2026, 9, 1));
        c.setEndDate(LocalDate.of(2027, 6, 30));
        return c;
    }

    @Test
    void saveAndFindById() {
        Course course = buildCourse("DAO_TEST_001", "DAO Test Course");
        courseDAO.save(course);
        createdIds.add(course.getId());

        assertNotNull(course.getId());

        Course found = courseDAO.findById(course.getId());
        assertNotNull(found);
        assertEquals("DAO_TEST_001", found.getCode());
        assertEquals("DAO Test Course", found.getName());
    }

    @Test
    void findByIdReturnsNullForNonExisting() {
        assertNull(courseDAO.findById(999999));
    }

    @Test
    void findAll() {
        Course course = buildCourse("DAO_TEST_FA", "DAO FindAll Course");
        courseDAO.save(course);
        createdIds.add(course.getId());

        List<Course> courses = courseDAO.findAll();
        assertNotNull(courses);
        assertTrue(courses.stream().anyMatch(c -> c.getId().equals(course.getId())));
    }

    @Test
    void findByCode() {
        Course course = buildCourse("DAO_TEST_CODE", "DAO Code Course");
        courseDAO.save(course);
        createdIds.add(course.getId());

        Course found = courseDAO.findByCode("DAO_TEST_CODE");
        assertNotNull(found);
        assertEquals(course.getId(), found.getId());
    }

    @Test
    void findByCodeReturnsNullForNonExisting() {
        assertNull(courseDAO.findByCode("DAO_NONEXISTENT_XYZ"));
    }

    @Test
    void findByName() {
        Course course = buildCourse("DAO_TEST_NM", "DAO Unique Name Course");
        courseDAO.save(course);
        createdIds.add(course.getId());

        Course found = courseDAO.findByName("DAO Unique Name Course");
        assertNotNull(found);
        assertEquals("DAO_TEST_NM", found.getCode());
    }

    @Test
    void findByNameReturnsNullForNonExisting() {
        assertNull(courseDAO.findByName("DAO Nonexistent Course Name XYZ"));
    }

    @Test
    void existsByCodeReturnsTrue() {
        Course course = buildCourse("DAO_TEST_EX", "DAO Exists Course");
        courseDAO.save(course);
        createdIds.add(course.getId());

        assertTrue(courseDAO.existsByCode("DAO_TEST_EX"));
    }

    @Test
    void existsByCodeReturnsFalse() {
        assertFalse(courseDAO.existsByCode("DAO_NONEXISTENT_CODE_XYZ"));
    }

    @Test
    void findByUserId() {
        Course course = buildCourse("DAO_TEST_UID", "DAO UserID Course");
        course.setUserId(1);
        courseDAO.save(course);
        createdIds.add(course.getId());

        List<Course> results = courseDAO.findByUserId(1);
        assertNotNull(results);
        assertTrue(results.stream().anyMatch(c -> c.getId().equals(course.getId())));
    }

    @Test
    void findByUserIdReturnsEmptyForNonExisting() {
        List<Course> results = courseDAO.findByUserId(999999);
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void findByGroupId() {
        Course course = buildCourse("DAO_TEST_GRP", "DAO Group Course");
        course.setAcademicGroupId(1);
        courseDAO.save(course);
        createdIds.add(course.getId());

        List<Course> results = courseDAO.findByGroupId(1);
        assertNotNull(results);
        assertTrue(results.stream().anyMatch(c -> c.getId().equals(course.getId())));
    }

    @Test
    void findByGroupIdReturnsEmptyForNonExisting() {
        List<Course> results = courseDAO.findByGroupId(999999);
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void update() {
        Course course = buildCourse("DAO_TEST_UPD", "Original Name");
        courseDAO.save(course);
        createdIds.add(course.getId());

        course.setName("Updated Name");
        courseDAO.update(course);

        Course updated = courseDAO.findById(course.getId());
        assertNotNull(updated);
        assertEquals("Updated Name", updated.getName());
    }

    @Test
    void delete() {
        Course course = buildCourse("DAO_TEST_DEL", "Delete Course");
        courseDAO.save(course);
        Integer id = course.getId();

        assertNotNull(courseDAO.findById(id));
        courseDAO.delete(id);
        assertNull(courseDAO.findById(id));
    }

    @Test
    void deleteNonExistingDoesNotThrow() {
        assertDoesNotThrow(() -> courseDAO.delete(999999));
    }

    @Test
    void saveDoesNotRollbackWhenTransactionNotActive() {
        EntityManagerFactory factory = mock(EntityManagerFactory.class);
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);

        when(factory.createEntityManager()).thenReturn(em);
        when(em.getTransaction()).thenReturn(tx);
        when(tx.isActive()).thenReturn(false);
        doThrow(new RuntimeException("forced")).when(tx).commit();

        try (MockedStatic<JPAUtil> mocked = mockStatic(JPAUtil.class)) {
            mocked.when(JPAUtil::getEntityManagerFactory).thenReturn(factory);
            assertThrows(RuntimeException.class, () -> courseDAO.save(new Course()));
        }

        verify(tx, never()).rollback();
        verify(em).close();
    }

    @Test
    void updateDoesNotRollbackWhenTransactionNotActive() {
        EntityManagerFactory factory = mock(EntityManagerFactory.class);
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);

        when(factory.createEntityManager()).thenReturn(em);
        when(em.getTransaction()).thenReturn(tx);
        when(tx.isActive()).thenReturn(false);
        doThrow(new RuntimeException("forced")).when(tx).commit();

        try (MockedStatic<JPAUtil> mocked = mockStatic(JPAUtil.class)) {
            mocked.when(JPAUtil::getEntityManagerFactory).thenReturn(factory);
            assertThrows(RuntimeException.class, () -> courseDAO.update(new Course()));
        }

        verify(tx, never()).rollback();
        verify(em).close();
    }

    @Test
    void deleteDoesNotRollbackWhenTransactionNotActive() {
        EntityManagerFactory factory = mock(EntityManagerFactory.class);
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);

        when(factory.createEntityManager()).thenReturn(em);
        when(em.getTransaction()).thenReturn(tx);
        when(tx.isActive()).thenReturn(false);
        doThrow(new RuntimeException("forced")).when(tx).commit();

        try (MockedStatic<JPAUtil> mocked = mockStatic(JPAUtil.class)) {
            mocked.when(JPAUtil::getEntityManagerFactory).thenReturn(factory);
            assertThrows(RuntimeException.class, () -> courseDAO.delete(1));
        }

        verify(tx, never()).rollback();
        verify(em).close();
    }
}
