package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import com.edujournal.entity.Grades;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Data access object for {@link Grades} entities.
 * Provides CRUD operations and grade search methods.
 */
public class GradesDAO {

    /**
     * Finds a grade by its identifier.
     *
     * @param id grade identifier
     * @return the found grade or null if not found
     */
    public Grades findById(int id) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return entityManager.find(Grades.class, id);
        } finally {
            entityManager.close();
        }
    }

    /**
     * Retrieves all grades associated with the specified enrollment.
     *
     * @param enrollmentId enrollment identifier
     * @return list of grades
     */
    public List<Grades> findByEnrollment(int enrollmentId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return entityManager
                    .createQuery(
                            "SELECT g FROM Grades g WHERE g.enrollmentId = :enrollmentId",
                            Grades.class)
                    .setParameter("enrollmentId", enrollmentId)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Retrieves all grades associated with the specified assessment.
     *
     * @param assessmentId assessment identifier
     * @return list of grades
     */
    public List<Grades> findByAssessment(int assessmentId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return entityManager
                    .createQuery(
                            "SELECT g FROM Grades g WHERE g.assessmentId = :assessmentId",
                            Grades.class)
                    .setParameter("assessmentId", assessmentId)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Finds a grade by enrollment and assessment identifiers.
     *
     * @param enrollmentId enrollment identifier
     * @param assessmentId assessment identifier
     * @return matching grade or null if not found
     */
    public Grades findByEnrollmentAssessment(int enrollmentId, int assessmentId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return entityManager
                    .createQuery(
                            "SELECT g FROM Grades g WHERE g.enrollmentId = :enrollmentId AND g.assessmentId = :assessmentId",
                            Grades.class)
                    .setParameter("enrollmentId", enrollmentId)
                    .setParameter("assessmentId", assessmentId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            entityManager.close();
        }
    }

    /**
     * Saves a new grade.
     *
     * @param grade grade to save
     */
    public void save(Grades grade) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(grade);
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            rollback(entityManager);
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Updates an existing grade.
     *
     * @param grade grade to update
     */
    public void update(Grades grade) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();
            grade = entityManager.merge(grade);
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            rollback(entityManager);
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Deletes a grade by its identifier.
     *
     * @param id grade identifier
     */
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
