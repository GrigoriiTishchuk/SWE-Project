package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import com.edujournal.entity.Assessments;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AssessmentsDAO {

    public Assessments findById(Integer id) {

        EntityManager entityManager = JPAUtil
                .getEntityManagerFactory()
                .createEntityManager();

        try {
            return entityManager.find(Assessments.class, id);

        } finally {
            entityManager.close();
        }
    }

    public List<Assessments> findAll() {

        EntityManager entityManager = JPAUtil
                .getEntityManagerFactory()
                .createEntityManager();

        try {
            return entityManager.createQuery(
                    "SELECT a FROM Assessments a",
                    Assessments.class
            ).getResultList();

        } finally {
            entityManager.close();
        }
    }

    public List<Assessments> findByCourseId(Integer courseId) {

        EntityManager entityManager = JPAUtil
                .getEntityManagerFactory()
                .createEntityManager();

        try {
            return entityManager.createQuery(
                            "SELECT a FROM Assessments a WHERE a.courseId = :courseId",
                            Assessments.class
                    )
                    .setParameter("courseId", courseId)
                    .getResultList();

        } finally {
            entityManager.close();
        }
    }

    public void save(Assessments assessment) {

        EntityManager entityManager = JPAUtil
                .getEntityManagerFactory()
                .createEntityManager();

        try {
            entityManager.getTransaction().begin();

            entityManager.persist(assessment);

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

    public void update(Assessments assessment) {

        EntityManager entityManager = JPAUtil
                .getEntityManagerFactory()
                .createEntityManager();

        try {
            entityManager.getTransaction().begin();

            entityManager.merge(assessment);

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

    public void delete(Assessments assessment) {

        EntityManager entityManager = JPAUtil
                .getEntityManagerFactory()
                .createEntityManager();

        try {
            entityManager.getTransaction().begin();

            entityManager.remove(
                    entityManager.contains(assessment)
                            ? assessment
                            : entityManager.merge(assessment)
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