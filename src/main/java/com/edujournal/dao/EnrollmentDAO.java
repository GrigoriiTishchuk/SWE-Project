package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import com.edujournal.entity.Enrollment;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Data access object for {@link Enrollment} entities.
 * Provides CRUD operations and enrollment search functionality.
 */
public class EnrollmentDAO {

    private static final String PARAM_STUDENT_ID = "studentId";
    private static final String PARAM_COURSE_ID = "courseId";
    private static final String PARAM_ACADEMIC_GROUP_ID = "academicGroupId";
    private static final String PARAM_GROUP_ID = "groupId";

    /**
     * Finds an enrollment by its identifier.
     *
     * @param id enrollment identifier
     * @return the found enrollment or null if not found
     */
    public Enrollment findById(Integer id) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager.find(Enrollment.class, id);
        } finally {
            entityManager.close();
        }
    }

    /**
     * Retrieves all enrollments.
     *
     * @return list of enrollments
     */
    public List<Enrollment> findAll() {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager
                    .createQuery("SELECT e FROM Enrollment e", Enrollment.class)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Retrieves all enrollments associated with the specified student.
     *
     * @param studentId student identifier
     * @return list of enrollments
     */
    public List<Enrollment> findByStudentId(Integer studentId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager
                    .createQuery(
                            "SELECT e FROM Enrollment e WHERE e.studentId = :studentId",
                            Enrollment.class)
                    .setParameter(PARAM_STUDENT_ID, studentId)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Retrieves all enrollments associated with the specified course.
     *
     * @param courseId course identifier
     * @return list of enrollments
     */
    public List<Enrollment> findByCourseId(Integer courseId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager
                    .createQuery(
                            "SELECT e FROM Enrollment e WHERE e.courseId = :courseId",
                            Enrollment.class)
                    .setParameter(PARAM_COURSE_ID, courseId)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Retrieves all enrollments belonging to the specified academic group.
     *
     * @param academicGroupId academic group identifier
     * @return list of enrollments
     */
    public List<Enrollment> findByAcademicGroupId(Integer academicGroupId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager
                    .createQuery(
                            "SELECT e FROM Enrollment e WHERE e.academicGroupId = :academicGroupId",
                            Enrollment.class)
                    .setParameter(PARAM_ACADEMIC_GROUP_ID, academicGroupId)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Retrieves enrollments matching the specified course and academic group.
     *
     * @param courseId course identifier
     * @param groupId academic group identifier
     * @return list of matching enrollments
     */
    public List<Enrollment> findByCourseAndGroup(int courseId, int groupId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                            "SELECT e FROM Enrollment e WHERE e.courseId = :courseId AND e.academicGroupId = :groupId",
                            Enrollment.class)
                    .setParameter(PARAM_COURSE_ID, courseId)
                    .setParameter(PARAM_GROUP_ID, groupId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Finds an enrollment for the specified student and course.
     *
     * @param studentId student identifier
     * @param courseId course identifier
     * @return matching enrollment or null if not found
     */
    public Enrollment findByStudentAndCourse(Integer studentId, Integer courseId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return em.createQuery(
                            "SELECT e FROM Enrollment e WHERE e.studentId = :studentId AND e.courseId = :courseId",
                            Enrollment.class)
                    .setParameter(PARAM_STUDENT_ID, studentId)
                    .setParameter(PARAM_COURSE_ID, courseId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

        } finally {
            em.close();
        }
    }

    /**
     * Saves a new enrollment.
     *
     * @param enrollment enrollment to save
     */
    public void save(Enrollment enrollment) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(enrollment);
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            rollback(entityManager);
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Updates an existing enrollment.
     *
     * @param enrollment enrollment to update
     */
    public void update(Enrollment enrollment) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();
            enrollment = entityManager.merge(enrollment);
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            rollback(entityManager);
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Deletes an enrollment by its identifier.
     *
     * @param id enrollment identifier
     */
    public void delete(Integer id) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Enrollment enrollment = entityManager.find(Enrollment.class, id);

            if (enrollment != null) {
                entityManager.remove(enrollment);
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
     * Deletes enrollments for the specified student
     * within the specified academic group.
     *
     * @param studentId student identifier
     * @param academicGroupId academic group identifier
     */
    public void deleteByStudentAndAcademicGroup(Integer studentId, Integer academicGroupId) {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();

            entityManager
                    .createQuery(
                            "DELETE FROM Enrollment e "
                                    + "WHERE e.studentId = :studentId "
                                    + "AND e.academicGroupId = :academicGroupId")
                    .setParameter(PARAM_STUDENT_ID, studentId)
                    .setParameter(PARAM_ACADEMIC_GROUP_ID, academicGroupId)
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
     * Deletes all enrollments associated with the specified course.
     *
     * @param courseId course identifier
     */
    public void deleteByCourseId(Integer courseId) {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();

            entityManager
                    .createQuery("DELETE FROM Enrollment e " + "WHERE e.courseId = :courseId")
                    .setParameter(PARAM_COURSE_ID, courseId)
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
     * Rolls back the current transaction if it is active.
     *
     * @param entityManager entity manager containing the transaction
     */
    private void rollback(EntityManager entityManager) {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }
}
