package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import com.edujournal.entity.Role;
import com.edujournal.entity.User;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Data access object for {@link User} entities.
 * Provides CRUD operations and user search methods.
 */
public class UserDAO {

    /**
     * Finds a user by identifier.
     *
     * @param userId user identifier
     * @return the found user or null if not found
     */
    public User findById(Integer userId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager.find(User.class, userId);

        } finally {
            entityManager.close();
        }
    }

    /**
     * Finds a user by username.
     *
     * @param username username
     * @return matching user or null if not found
     */
    public User findByUsername(String username) {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager
                    .createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

        } finally {
            entityManager.close();
        }
    }

    /**
     * Retrieves all users with the specified role.
     *
     * @param role user role
     * @return list of matching users
     */
    public List<User> findByRole(Role role) {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager
                    .createQuery("SELECT u FROM User u WHERE u.role = :role", User.class)
                    .setParameter("role", role)
                    .getResultList();

        } finally {
            entityManager.close();
        }
    }

    /**
     * Retrieves all users.
     *
     * @return list of users
     */
    public List<User> findAll() {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();

        } finally {
            entityManager.close();
        }
    }

    /**
     * Retrieves all users with the teacher role.
     *
     * @return list of teachers
     */
    public List<User> findAllTeachers() {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager
                    .createQuery(
                            "SELECT u FROM User u WHERE u.role = com.edujournal.entity.Role.TEACHER",
                            User.class)
                    .getResultList();

        } finally {
            entityManager.close();
        }
    }

    /**
     * Saves a new user.
     *
     * @param user user to save
     */
    public void save(User user) {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            rollback(entityManager);
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Updates an existing user.
     *
     * @param user user to update
     */
    public void update(User user) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();

            user = entityManager.merge(user);

            entityManager.getTransaction().commit();

        } catch (Exception e) {
            rollback(entityManager);
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Updates the preferred locale of a user.
     *
     * @param userId user identifier
     * @param locale locale code to set
     */
    public void updateLocale(Integer userId, String locale) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager
                    .createQuery("UPDATE User u SET u.preferredLocale = :locale WHERE u.id = :id")
                    .setParameter("locale", locale)
                    .setParameter("id", userId)
                    .executeUpdate();
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            rollback(entityManager);
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Deletes the specified user.
     *
     * @param user user to delete
     */
    public void delete(User user) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();

            entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));

            entityManager.getTransaction().commit();

        } catch (Exception e) {
            rollback(entityManager);
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Rolls back the active transaction if one exists.
     *
     * @param entityManager entity manager containing the transaction
     */
    private void rollback(EntityManager entityManager) {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }
}
