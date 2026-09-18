package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import com.edujournal.entity.Course;
import jakarta.persistence.EntityManager;

import java.util.List;

public class CourseDAO {

    public Course findById(Integer id) {

        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            return entityManager.find(Course.class, id);

        } finally {
            entityManager.close();
        }
    }

    public List<Course> findAll() {

        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            return entityManager.createQuery(
                    "SELECT c FROM Course c",
                    Course.class
            ).getResultList();

        } finally {
            entityManager.close();
        }
    }

    public Course findByName(String name) {

        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            return entityManager.createQuery(
                            "SELECT c FROM Course c WHERE c.name = :name",
                            Course.class
                    )
                    .setParameter("name", name)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

        } finally {
            entityManager.close();
        }
    }

    public Course findByCode(String code) {

        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            return entityManager.createQuery(
                            "SELECT c FROM Course c WHERE c.code = :code",
                            Course.class
                    )
                    .setParameter("code", code)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

        } finally {
            entityManager.close();
        }
    }

    public boolean existsByCode(String code) {
        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();
        try{
            return entityManager.createQuery(
                            "SELECT COUNT(c) FROM Course c WHERE c.code = :code", Long.class)
                    .setParameter("code", code)
                    .getSingleResult() > 0;
        } finally {
            entityManager.close();
        }
    }

    public List<Course> findByUserId(Integer userId) {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager.createQuery(
                            "SELECT c FROM Course c WHERE c.userId = :userId",
                            Course.class
                    )
                    .setParameter("userId", userId)
                    .getResultList();

        } finally {
            entityManager.close();
        }
    }

    public void save(Course course) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(course);
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

    public void update(Course course) {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(course);
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

    public void delete(Integer id) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();

            Course course = entityManager.find(Course.class, id);
            if (course != null) {
                entityManager.remove(course);
            }
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