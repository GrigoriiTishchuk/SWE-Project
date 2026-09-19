package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import com.edujournal.entity.Role;
import com.edujournal.entity.User;
import jakarta.persistence.EntityManager;

import java.util.List;

public class UserDAO {

    public User findById(Integer userId) {
        EntityManager entityManager = JPAUtil
                .getEntityManagerFactory()
                .createEntityManager();

        try {
            return entityManager.find(User.class, userId);

        } finally {
            entityManager.close();
        }
    }

    public User findByUsername(String username) {

        EntityManager entityManager = JPAUtil
                .getEntityManagerFactory()
                .createEntityManager();

        try {
            return entityManager.createQuery(
                            "SELECT u FROM User u WHERE u.username = :username",
                            User.class
                    )
                    .setParameter("username", username)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

        } finally {
            entityManager.close();
        }
    }

    public List<User> findByRole(Role role) {

        EntityManager entityManager = JPAUtil
                .getEntityManagerFactory()
                .createEntityManager();

        try {
            return entityManager.createQuery(
                            "SELECT u FROM User u WHERE u.role = :role",
                            User.class
                    )
                    .setParameter("role", role)
                    .getResultList();

        } finally {
            entityManager.close();
        }
    }

    public List<User> findAll() {

        EntityManager entityManager = JPAUtil
                .getEntityManagerFactory()
                .createEntityManager();

        try {
            return entityManager.createQuery(
                            "SELECT u FROM User u",
                            User.class
                    )
                    .getResultList();

        } finally {
            entityManager.close();
        }
    }

    public List<User> findAllTeachers() {

        EntityManager entityManager = JPAUtil
                .getEntityManagerFactory()
                .createEntityManager();

        try {
            return entityManager.createQuery(
                            "SELECT u FROM User u WHERE u.role = com.edujournal.entity.Role.TEACHER",
                            User.class
                    )
                    .getResultList();

        } finally {
            entityManager.close();
        }
    }

    public void save(User user) {

        EntityManager entityManager = JPAUtil
                .getEntityManagerFactory()
                .createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();

        } catch (Exception e) {

            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;

        } finally {
            entityManager.close();
        }
    }

    public void update(User user) {
        EntityManager entityManager = JPAUtil
                .getEntityManagerFactory()
                .createEntityManager();

        try {
            entityManager.getTransaction().begin();

            entityManager.merge(user);

            entityManager.getTransaction().commit();

        } catch (Exception e) {

            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;

        } finally {
            entityManager.close();
        }
    }

    public void delete(User user) {
        EntityManager entityManager = JPAUtil
                .getEntityManagerFactory()
                .createEntityManager();

        try {
            entityManager.getTransaction().begin();

            entityManager.remove(
                    entityManager.contains(user)
                            ? user
                            : entityManager.merge(user)
            );

            entityManager.getTransaction().commit();

        } catch (Exception e) {

            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;

        } finally {
            entityManager.close();
        }
    }
}