package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.entity.Student;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Data access object for {@link AcademicGroup} entities.
 * Provides CRUD operations and group membership management.
 */
public class AcademicGroupDAO {

    /**
     * Finds an academic group by its identifier.
     *
     * @param id group identifier
     * @return the found academic group or null if it does not exist
     */
    public AcademicGroup findById(Integer id) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager.find(AcademicGroup.class, id);
        } finally {
            entityManager.close();
        }
    }

    /**
     * Returns all academic groups.
     *
     * @return list of academic groups
     */
    public List<AcademicGroup> findAll() {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager
                    .createQuery("SELECT a FROM AcademicGroup a", AcademicGroup.class)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Finds identifiers of all students belonging to the specified group.
     *
     * @param groupId group identifier
     * @return list of student identifiers
     */
    public List<Integer> findStudentIdsByGroupId(Integer groupId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager
                    .createQuery(
                            "SELECT s.id FROM Student s WHERE s.academicGroupId = :groupId",
                            Integer.class)
                    .setParameter("groupId", groupId)
                    .getResultList();

        } finally {
            entityManager.close();
        }
    }

    /**
     * Finds an academic group by its name.
     *
     * @param name group name
     * @return the found academic group or null if not found
     */
    public AcademicGroup findByName(String name) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager
                    .createQuery(
                            "SELECT a FROM AcademicGroup a WHERE a.name = :name",
                            AcademicGroup.class)
                    .setParameter("name", name)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            entityManager.close();
        }
    }

    /**
     * Assigns a student to the specified academic group.
     *
     * @param studentId student identifier
     * @param groupId group identifier
     */
    public void addStudentToGroup(Integer studentId, Integer groupId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Student student = entityManager.find(Student.class, studentId);

            if (student != null) {
                student.setAcademicGroupId(groupId);
                student = entityManager.merge(student);
            }

            entityManager.getTransaction().commit();

        } finally {
            entityManager.close();
        }
    }

    /**
     * Removes a student from the specified academic group.
     *
     * @param studentId student identifier
     * @param groupId group identifier
     */
    public void removeStudentFromGroup(Integer studentId, Integer groupId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Student student = entityManager.find(Student.class, studentId);

            if (student != null && groupId.equals(student.getAcademicGroupId())) {
                student.setAcademicGroupId(null);
                student = entityManager.merge(student);
            }

            entityManager.getTransaction().commit();

        } finally {
            entityManager.close();
        }
    }

    /**
     * Persists a new academic group.
     *
     * @param academicGroup academic group to save
     */
    public void save(AcademicGroup academicGroup) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(academicGroup);
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

    /**
     * Updates an existing academic group.
     *
     * @param academicGroup academic group to update
     */
    public void update(AcademicGroup academicGroup) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();
            academicGroup = entityManager.merge(academicGroup);
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

    /**
     * Deletes an academic group by its identifier.
     *
     * @param id group identifier
     */
    public void delete(Integer id) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();

            AcademicGroup academicGroup = entityManager.find(AcademicGroup.class, id);

            if (academicGroup != null) {
                entityManager.remove(academicGroup);
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
