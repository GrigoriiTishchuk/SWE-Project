package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import com.edujournal.entity.Student;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Data access object for {@link Student} entities.
 * Provides CRUD operations and student search functionality.
 */
public class StudentDAO {

    /**
     * Finds a student by identifier.
     *
     * @param id student identifier
     * @return the found student or null if not found
     */
    public Student findById(Integer id) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager.find(Student.class, id);
        } finally {
            entityManager.close();
        }
    }

    /**
     * Retrieves all students.
     *
     * @return list of students
     */
    public List<Student> findAll() {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager
                    .createQuery("SELECT s FROM Student s", Student.class)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Finds a student associated with the specified user.
     *
     * @param userId user identifier
     * @return matching student or null if not found
     */
    public Student findByUserId(Integer userId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return entityManager
                    .createQuery("SELECT s FROM Student s WHERE s.userId = :userId", Student.class)
                    .setParameter("userId", userId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            entityManager.close();
        }
    }

    /**
     * Finds a student by student number.
     *
     * @param studentNumber student number
     * @return matching student or null if not found
     */
    public Student findByStudentNumber(String studentNumber) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return entityManager
                    .createQuery(
                            "SELECT s FROM Student s WHERE s.studentNumber = :studentNumber",
                            Student.class)
                    .setParameter("studentNumber", studentNumber)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            entityManager.close();
        }
    }

    /**
     * Saves a new student.
     *
     * @param student student to save
     */
    public void save(Student student) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(student);
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            rollback(entityManager);
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Updates an existing student.
     *
     * @param student student to update
     */
    public void update(Student student) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();
            student = entityManager.merge(student);
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            rollback(entityManager);
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Deletes a student by identifier.
     *
     * @param id student identifier
     */
    public void delete(Integer id) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Student student = entityManager.find(Student.class, id);

            if (student != null) {
                entityManager.remove(student);
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
