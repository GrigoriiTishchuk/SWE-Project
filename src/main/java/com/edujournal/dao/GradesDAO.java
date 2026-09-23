package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import com.edujournal.entity.Grades;
import jakarta.persistence.EntityManager;

import java.util.List;

public class GradesDAO {
    public Grades findById(int id) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return entityManager.find(Grades.class, id);
        } finally {
            entityManager.close();
        }
    }

    public List<Grades> findByEnrollment(int enrollmentId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return entityManager.createQuery(
                            "SELECT g FROM Grades g WHERE g.enrollmentId = :enrollmentId",
                            Grades.class
                    )
                    .setParameter("enrollmentId", enrollmentId)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    public List<Grades> findByAssessment(int assessmentId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return entityManager.createQuery(
                            "SELECT g FROM Grades g WHERE g.assessmentId = :assessmentId",
                            Grades.class
                    )
                    .setParameter("assessmentId", assessmentId)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    public Grades findByEnrollmentAssessment(int enrollmentId, int assessmentId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return entityManager.createQuery(
                    "SELECT g FROM Grades g WHERE g.enrollmentId = :enrollmentId AND g.assessmentId = :assessmentId",
                    Grades.class
            )
                    .setParameter("enrollmentId", enrollmentId)
                    .setParameter("assessmentId", assessmentId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            entityManager.close();
        }
    }

    public void save(Grades grade) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(grade);
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

    public void update(Grades grade) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(grade);
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

    public void deleteById(int id) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            Grades grade = entityManager.find(Grades.class, id);
            if (grade != null) {
                entityManager.getTransaction().begin();
                entityManager.remove(grade);
                entityManager.getTransaction().commit();
            }

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
