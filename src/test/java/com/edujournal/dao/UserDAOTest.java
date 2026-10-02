package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import com.edujournal.entity.Role;
import com.edujournal.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDAOTest {

    private final UserDAO userDAO = new UserDAO();

    private final List<User> createdUsers = new ArrayList<>();

    @AfterEach
    void cleanup() {
        for (User user : createdUsers) {
            try { userDAO.delete(user); } catch (Exception ignored) {}
        }
        createdUsers.clear();
    }

    private User buildUser(String username, Role role) {
        User u = new User();
        u.setUsername(username);
        u.setPasswordHash("test_hash");
        u.setFirstName("Test");
        u.setLastName("User");
        u.setRole(role);
        return u;
    }

    @Test
    void saveAndFindById() {
        User user = buildUser("dao_test_user_1", Role.TEACHER);
        userDAO.save(user);
        createdUsers.add(user);

        assertNotNull(user.getId());

        User found = userDAO.findById(user.getId());
        assertNotNull(found);
        assertEquals("dao_test_user_1", found.getUsername());
        assertEquals(Role.TEACHER, found.getRole());
    }

    @Test
    void findByIdReturnsNullForNonExisting() {
        assertNull(userDAO.findById(999999));
    }

    @Test
    void findByUsername() {
        User user = buildUser("dao_test_user_2", Role.STUDENT);
        userDAO.save(user);
        createdUsers.add(user);

        User found = userDAO.findByUsername("dao_test_user_2");
        assertNotNull(found);
        assertEquals(user.getId(), found.getId());
    }

    @Test
    void findByUsernameReturnsNullForNonExisting() {
        assertNull(userDAO.findByUsername("dao_nonexistent_user_xyz_999"));
    }

    @Test
    void findByRole() {
        User user = buildUser("dao_test_teacher_role", Role.TEACHER);
        userDAO.save(user);
        createdUsers.add(user);

        List<User> teachers = userDAO.findByRole(Role.TEACHER);
        assertNotNull(teachers);
        assertTrue(teachers.stream().anyMatch(u -> u.getId().equals(user.getId())));
    }

    @Test
    void findAll() {
        User user = buildUser("dao_test_findall_user", Role.STUDENT);
        userDAO.save(user);
        createdUsers.add(user);

        List<User> all = userDAO.findAll();
        assertNotNull(all);
        assertTrue(all.stream().anyMatch(u -> u.getId().equals(user.getId())));
    }

    @Test
    void findAllTeachers() {
        User teacher = buildUser("dao_test_teacher_all", Role.TEACHER);
        userDAO.save(teacher);
        createdUsers.add(teacher);

        List<User> teachers = userDAO.findAllTeachers();
        assertNotNull(teachers);
        assertTrue(teachers.stream().anyMatch(u -> u.getId().equals(teacher.getId())));
    }

    @Test
    void update() {
        User user = buildUser("dao_test_update_user", Role.TEACHER);
        userDAO.save(user);
        createdUsers.add(user);

        user.setFirstName("Updated");
        userDAO.update(user);

        User updated = userDAO.findById(user.getId());
        assertNotNull(updated);
        assertEquals("Updated", updated.getFirstName());
    }

    @Test
    void delete() {
        User user = buildUser("dao_test_delete_user", Role.STUDENT);
        userDAO.save(user);
        Integer id = user.getId();

        assertNotNull(userDAO.findById(id));
        userDAO.delete(user);
        assertNull(userDAO.findById(id));
    }

    @Test
    void saveThrowsForNullUser() {
        assertThrows(Exception.class, () -> userDAO.save(null));
    }

    @Test
    void updateThrowsForNullUser() {
        assertThrows(Exception.class, () -> userDAO.update(null));
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
            assertThrows(RuntimeException.class, () -> userDAO.save(new User()));
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
            assertThrows(RuntimeException.class, () -> userDAO.update(new User()));
        }

        verify(tx, never()).rollback();
        verify(em).close();
    }

    @Test
    void deleteDoesNotRollbackWhenTransactionNotActive() {
        EntityManagerFactory factory = mock(EntityManagerFactory.class);
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);
        User user = new User();

        when(factory.createEntityManager()).thenReturn(em);
        when(em.getTransaction()).thenReturn(tx);
        when(em.contains(user)).thenReturn(false);
        when(em.merge(user)).thenReturn(user);
        when(tx.isActive()).thenReturn(false);
        doThrow(new RuntimeException("forced")).when(tx).commit();

        try (MockedStatic<JPAUtil> mocked = mockStatic(JPAUtil.class)) {
            mocked.when(JPAUtil::getEntityManagerFactory).thenReturn(factory);
            assertThrows(RuntimeException.class, () -> userDAO.delete(user));
        }

        verify(tx, never()).rollback();
        verify(em).close();
    }

    @Test
    void deleteRemovesDirectlyWhenEntityIsContained() {
        EntityManagerFactory factory = mock(EntityManagerFactory.class);
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);
        User user = new User();

        when(factory.createEntityManager()).thenReturn(em);
        when(em.getTransaction()).thenReturn(tx);
        when(em.contains(user)).thenReturn(true);

        try (MockedStatic<JPAUtil> mocked = mockStatic(JPAUtil.class)) {
            mocked.when(JPAUtil::getEntityManagerFactory).thenReturn(factory);
            assertDoesNotThrow(() -> userDAO.delete(user));
        }

        verify(em).remove(user);
        verify(em, never()).merge(user);
        verify(em).close();
    }
}
