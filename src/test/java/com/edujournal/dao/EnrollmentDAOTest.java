package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import com.edujournal.entity.Enrollment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EnrollmentDAOTest {

    private final EnrollmentDAO enrollmentDAO =
            new EnrollmentDAO();

    private final List<Integer> createdIds =
            new ArrayList<>();

    @AfterEach
    void cleanup() {
        for (Integer id : createdIds) {
            enrollmentDAO.delete(id);
        }

        createdIds.clear();
    }

    @Test
    void saveAndFindById() {
        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(1);
        enrollment.setCourseId(1);
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ACTIVE");

        enrollmentDAO.save(enrollment);

        createdIds.add(enrollment.getId());

        assertNotNull(enrollment.getId());

        Enrollment found =
                enrollmentDAO.findById(enrollment.getId());

        assertNotNull(found);
        assertEquals(1, found.getStudentId());
        assertEquals(1, found.getCourseId());
        assertEquals(1, found.getAcademicGroupId());
        assertEquals("ACTIVE", found.getStatus());
    }

    @Test
    void findAll() {
        List<Enrollment> enrollments =
                enrollmentDAO.findAll();

        assertNotNull(enrollments);
    }

    @Test
    void findByStudentId() {
        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(1);
        enrollment.setCourseId(1);
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ACTIVE");

        enrollmentDAO.save(enrollment);

        createdIds.add(enrollment.getId());

        List<Enrollment> results =
                enrollmentDAO.findByStudentId(1);

        assertFalse(results.isEmpty());

        assertTrue(
                results.stream()
                        .anyMatch(e ->
                                e.getId().equals(enrollment.getId())
                        )
        );
    }

    @Test
    void findByCourseId() {
        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(2);
        enrollment.setCourseId(1);
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ACTIVE");

        enrollmentDAO.save(enrollment);

        createdIds.add(enrollment.getId());

        List<Enrollment> results =
                enrollmentDAO.findByCourseId(1);

        assertFalse(results.isEmpty());

        assertTrue(
                results.stream()
                        .anyMatch(e ->
                                e.getId().equals(enrollment.getId())
                        )
        );
    }

    @Test
    void findByAcademicGroupId() {
        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(3);
        enrollment.setCourseId(1);
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ACTIVE");

        enrollmentDAO.save(enrollment);

        createdIds.add(enrollment.getId());

        List<Enrollment> results =
                enrollmentDAO.findByAcademicGroupId(1);

        assertFalse(results.isEmpty());

        assertTrue(
                results.stream()
                        .anyMatch(e ->
                                e.getId().equals(enrollment.getId())
                        )
        );
    }

    @Test
    void update() {
        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(4);
        enrollment.setCourseId(1);
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ACTIVE");

        enrollmentDAO.save(enrollment);

        createdIds.add(enrollment.getId());

        enrollment.setStatus("INACTIVE");

        enrollmentDAO.update(enrollment);

        Enrollment updated =
                enrollmentDAO.findById(enrollment.getId());

        assertNotNull(updated);
        assertEquals("INACTIVE", updated.getStatus());
    }

    @Test
    void delete() {
        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(4);
        enrollment.setCourseId(1);
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ACTIVE");

        enrollmentDAO.save(enrollment);

        Integer id = enrollment.getId();

        assertNotNull(enrollmentDAO.findById(id));

        enrollmentDAO.delete(id);

        assertNull(enrollmentDAO.findById(id));
    }

    @Test
    void findByCourseAndGroup() {
        System.out.println(">>> findByCourseAndGroup TEST CALISTI <<<");

        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(1);
        enrollment.setCourseId(1);
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ACTIVE");

        enrollmentDAO.save(enrollment);

        createdIds.add(enrollment.getId());

        List<Enrollment> results =
                enrollmentDAO.findByCourseAndGroup(1, 1);

        assertNotNull(results);

        assertTrue(
                results.stream()
                        .anyMatch(e ->
                                e.getId().equals(enrollment.getId())
                        )
        );
    }

    @Test
    void findByCourseAndGroupReturnsEmptyListForNonExistingCombination() {
        List<Enrollment> results =
                enrollmentDAO.findByCourseAndGroup(999999, 999999);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void findByStudentAndCourse() {
        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(2);
        enrollment.setCourseId(1);
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ACTIVE");

        enrollmentDAO.save(enrollment);

        createdIds.add(enrollment.getId());

        Enrollment found =
                enrollmentDAO.findByStudentAndCourse(2, 1);

        assertNotNull(found);
        assertEquals(enrollment.getId(), found.getId());
        assertEquals(2, found.getStudentId());
        assertEquals(1, found.getCourseId());
    }

    @Test
    void findByStudentAndCourseReturnsNullForNonExistingCombination() {
        Enrollment found =
                enrollmentDAO.findByStudentAndCourse(999999, 999999);

        assertNull(found);
    }

    @Test
    void deleteByStudentAndAcademicGroup() {
        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(2);
        enrollment.setCourseId(1);
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ACTIVE");

        enrollmentDAO.save(enrollment);

        Integer id = enrollment.getId();
        createdIds.add(id);

        assertNotNull(enrollmentDAO.findById(id));

        enrollmentDAO.deleteByStudentAndAcademicGroup(2, 1);

        assertNull(enrollmentDAO.findById(id));
    }

    @Test
    void deleteByCourseId() {
        enrollmentDAO.deleteByCourseId(999999);

        assertTrue(
                enrollmentDAO.findByCourseId(999999).isEmpty()
        );
    }

    @Test
    void saveThrowsExceptionForNullEnrollment() {
        assertThrows(
                Exception.class,
                () -> enrollmentDAO.save(null)
        );
    }

    @Test
    void updateThrowsExceptionForNullEnrollment() {
        assertThrows(
                Exception.class,
                () -> enrollmentDAO.update(null)
        );
    }

    @Test
    void deleteThrowsExceptionForNullId() {
        assertThrows(
                Exception.class,
                () -> enrollmentDAO.delete(null)
        );
    }

    @Test
    void saveDoesNotRollbackWhenTransactionNotActive() {
        EntityManagerFactory factory = mock(EntityManagerFactory.class);
        EntityManager entityManager = mock(EntityManager.class);
        EntityTransaction transaction = mock(EntityTransaction.class);

        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(transaction.isActive()).thenReturn(false);
        doThrow(new RuntimeException("forced")).when(transaction).commit();

        try (MockedStatic<JPAUtil> mocked = mockStatic(JPAUtil.class)) {
            mocked.when(JPAUtil::getEntityManagerFactory).thenReturn(factory);
            assertThrows(RuntimeException.class, () -> enrollmentDAO.save(new Enrollment()));
        }

        verify(transaction, never()).rollback();
        verify(entityManager).close();
    }

    @Test
    void updateDoesNotRollbackWhenTransactionNotActive() {
        EntityManagerFactory factory = mock(EntityManagerFactory.class);
        EntityManager entityManager = mock(EntityManager.class);
        EntityTransaction transaction = mock(EntityTransaction.class);

        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(transaction.isActive()).thenReturn(false);
        doThrow(new RuntimeException("forced")).when(transaction).commit();

        try (MockedStatic<JPAUtil> mocked = mockStatic(JPAUtil.class)) {
            mocked.when(JPAUtil::getEntityManagerFactory).thenReturn(factory);
            assertThrows(RuntimeException.class, () -> enrollmentDAO.update(new Enrollment()));
        }

        verify(transaction, never()).rollback();
        verify(entityManager).close();
    }

    @Test
    void deleteDoesNotRollbackWhenTransactionNotActive() {
        EntityManagerFactory factory = mock(EntityManagerFactory.class);
        EntityManager entityManager = mock(EntityManager.class);
        EntityTransaction transaction = mock(EntityTransaction.class);

        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(transaction.isActive()).thenReturn(false);
        doThrow(new RuntimeException("forced")).when(transaction).commit();

        try (MockedStatic<JPAUtil> mocked = mockStatic(JPAUtil.class)) {
            mocked.when(JPAUtil::getEntityManagerFactory).thenReturn(factory);
            assertThrows(RuntimeException.class, () -> enrollmentDAO.delete(1));
        }

        verify(transaction, never()).rollback();
        verify(entityManager).close();
    }

    @Test
    void deleteByStudentAndAcademicGroupRollsBackOnException() {
        EntityManagerFactory factory = mock(EntityManagerFactory.class);
        EntityManager entityManager = mock(EntityManager.class);
        EntityTransaction transaction = mock(EntityTransaction.class);
        Query query = mock(Query.class);

        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(transaction.isActive()).thenReturn(true);
        when(entityManager.createQuery(anyString())).thenReturn(query);

        doThrow(new RuntimeException("test exception"))
                .when(query)
                .executeUpdate();

        try (MockedStatic<JPAUtil> mocked = mockStatic(JPAUtil.class)) {
            mocked.when(JPAUtil::getEntityManagerFactory)
                    .thenReturn(factory);

            assertThrows(
                    RuntimeException.class,
                    () -> enrollmentDAO.deleteByStudentAndAcademicGroup(1, 1)
            );
        }

        verify(transaction).rollback();
        verify(entityManager).close();
    }

    @Test
    void deleteByCourseIdRollsBackOnException() {
        EntityManagerFactory factory = mock(EntityManagerFactory.class);
        EntityManager entityManager = mock(EntityManager.class);
        EntityTransaction transaction = mock(EntityTransaction.class);
        Query query = mock(Query.class);

        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(transaction.isActive()).thenReturn(true);
        when(entityManager.createQuery(anyString())).thenReturn(query);

        doThrow(new RuntimeException("test exception"))
                .when(query)
                .executeUpdate();

        try (MockedStatic<JPAUtil> mocked = mockStatic(JPAUtil.class)) {
            mocked.when(JPAUtil::getEntityManagerFactory)
                    .thenReturn(factory);

            assertThrows(
                    RuntimeException.class,
                    () -> enrollmentDAO.deleteByCourseId(1)
            );
        }

        verify(transaction).rollback();
        verify(entityManager).close();
    }

    @Test
    void deleteByStudentAndAcademicGroupDoesNotRollbackWhenTransactionNotActive() {
        EntityManagerFactory factory = mock(EntityManagerFactory.class);
        EntityManager entityManager = mock(EntityManager.class);
        EntityTransaction transaction = mock(EntityTransaction.class);
        Query query = mock(Query.class);

        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(transaction.isActive()).thenReturn(false);
        when(entityManager.createQuery(anyString())).thenReturn(query);
        doThrow(new RuntimeException("forced")).when(query).executeUpdate();

        try (MockedStatic<JPAUtil> mocked = mockStatic(JPAUtil.class)) {
            mocked.when(JPAUtil::getEntityManagerFactory).thenReturn(factory);
            assertThrows(RuntimeException.class,
                    () -> enrollmentDAO.deleteByStudentAndAcademicGroup(1, 1));
        }

        verify(transaction, never()).rollback();
        verify(entityManager).close();
    }

    @Test
    void deleteByCourseIdDoesNotRollbackWhenTransactionNotActive() {
        EntityManagerFactory factory = mock(EntityManagerFactory.class);
        EntityManager entityManager = mock(EntityManager.class);
        EntityTransaction transaction = mock(EntityTransaction.class);
        Query query = mock(Query.class);

        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(transaction.isActive()).thenReturn(false);
        when(entityManager.createQuery(anyString())).thenReturn(query);
        doThrow(new RuntimeException("forced")).when(query).executeUpdate();

        try (MockedStatic<JPAUtil> mocked = mockStatic(JPAUtil.class)) {
            mocked.when(JPAUtil::getEntityManagerFactory).thenReturn(factory);
            assertThrows(RuntimeException.class, () -> enrollmentDAO.deleteByCourseId(1));
        }

        verify(transaction, never()).rollback();
        verify(entityManager).close();
    }
}