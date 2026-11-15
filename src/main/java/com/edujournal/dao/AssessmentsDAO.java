package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import com.edujournal.entity.Assessments;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Data access object for {@link Assessments} entities.
 * Provides CRUD operations and assessment queries.
 */
public class AssessmentsDAO {

    /**
     * Finds an assessment by its identifier.
     *
     * @param id assessment identifier
     * @return the found assessment or null if not found
     */
    public Assessments findById(Integer id) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return entityManager.find(Assessments.class, id);
        } finally {
            entityManager.close();
        }
    }

    /**
     * Retrieves all assessments.
     *
     * @return list of assessments
     */
    public List<Assessments> findAll() {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return entityManager
                    .createQuery("SELECT a FROM Assessments a", Assessments.class)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Retrieves all assessments associated with a course.
     *
     * @param courseId course identifier
     * @return list of course assessments
     */
    public List<Assessments> findByCourseId(Integer courseId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return entityManager
                    .createQuery(
                            "SELECT a FROM Assessments a WHERE a.courseId = :courseId",
                            Assessments.class)
                    .setParameter("courseId", courseId)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Persists a new assessment.
     *
     * @param assessment assessment to save
     */
    public void save(Assessments assessment) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(assessment);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            rollback(entityManager);
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Updates an existing assessment.
     *
     * @param assessment assessment to update
     */
    public void update(Assessments assessment) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            assessment = entityManager.merge(assessment);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            rollback(entityManager);
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     **Deletes the specified assessment.
     **
     * @param assessment assessment to delete
     */
    public void delete(Assessments assessment) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();

            Assessments managedAssessment =
                    entityManager.find(Assessments.class, assessment.getId());

            if (managedAssessment != null) {
                entityManager.remove(managedAssessment);
            }

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
