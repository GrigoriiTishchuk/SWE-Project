package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import com.edujournal.entity.Course;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Data access object for {@link Course} entities.
 * Provides CRUD operations and course search methods.
 */
public class CourseDAO {

    /**
     * Finds a course by its identifier.
     *
     * @param id course identifier
     * @return the found course or null if not found
     */
    public Course findById(Integer id) {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager.find(Course.class, id);

        } finally {
            entityManager.close();
        }
    }

    /**
     * Returns all courses.
     *
     * @return list of courses
     */
    public List<Course> findAll() {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager
                    .createQuery("SELECT c FROM Course c", Course.class)
                    .getResultList();

        } finally {
            entityManager.close();
        }
    }

    /**
     * Finds a course by its name and academic group.
     *
     * @param name course name
     * @param groupId academic group identifier
     * @return matching course or null if not found
     */
    public Course findByNameAndGroupId(String name, Integer groupId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return entityManager
                    .createQuery(
                            "SELECT c FROM Course c WHERE c.name = :name AND c.academicGroupId = :groupId",
                            Course.class)
                    .setParameter("name", name)
                    .setParameter("groupId", groupId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            entityManager.close();
        }
    }

    /**
     * Finds a course by its name.
     *
     * @param name course name
     * @return matching course or null if not found
     */
    public Course findByName(String name) {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager
                    .createQuery("SELECT c FROM Course c WHERE c.name = :name", Course.class)
                    .setParameter("name", name)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

        } finally {
            entityManager.close();
        }
    }

    /**
     * Finds a course by its code.
     *
     * @param code course code
     * @return matching course or null if not found
     */

    public Course findByCode(String code) {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager
                    .createQuery("SELECT c FROM Course c WHERE c.code = :code", Course.class)
                    .setParameter("code", code)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

        } finally {
            entityManager.close();
        }
    }

    /**
     * Checks whether a course with the specified code exists.
     *
     * @param code course code
     * @return true if a matching course exists, false otherwise
     */
    public boolean existsByCode(String code) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return entityManager
                            .createQuery(
                                    "SELECT COUNT(c) FROM Course c WHERE c.code = :code",
                                    Long.class)
                            .setParameter("code", code)
                            .getSingleResult()
                    > 0;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Returns all courses taught by the specified user.
     *
     * @param userId user identifier
     * @return list of courses
     */
    public List<Course> findByUserId(Integer userId) {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager
                    .createQuery("SELECT c FROM Course c WHERE c.userId = :userId", Course.class)
                    .setParameter("userId", userId)
                    .getResultList();

        } finally {
            entityManager.close();
        }
    }

    /**
     * Returns all courses assigned to the specified academic group.
     *
     * @param groupId academic group identifier
     * @return list of courses
     */
    public List<Course> findByGroupId(Integer groupId) {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager
                    .createQuery(
                            "SELECT c FROM Course c WHERE c.academicGroupId = :groupId",
                            Course.class)
                    .setParameter("groupId", groupId)
                    .getResultList();

        } finally {
            entityManager.close();
        }
    }

    /**
     * Saves a new course.
     *
     * @param course course to save
     */
    public void save(Course course) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(course);
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            rollback(entityManager);
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Updates an existing course.
     *
     * @param course course to update
     */
    public void update(Course course) {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            course = entityManager.merge(course);
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            rollback(entityManager);
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Deletes a course by its identifier.
     *
     * @param id course identifier
     */
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
