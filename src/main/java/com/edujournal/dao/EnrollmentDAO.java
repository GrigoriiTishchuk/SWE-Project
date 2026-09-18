package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import com.edujournal.entity.Enrollment;
import jakarta.persistence.EntityManager;

import java.util.List;

public class EnrollmentDAO {

    public Enrollment findById(Integer id) {
        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            return entityManager.find(Enrollment.class, id);
        } finally {
            entityManager.close();
        }
    }

    public List<Enrollment> findAll() {
        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            return entityManager.createQuery(
                    "SELECT e FROM Enrollment e",
                    Enrollment.class
            ).getResultList();
        } finally {
            entityManager.close();
        }
    }

    public List<Enrollment> findByStudentId(Integer studentId) {
        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            return entityManager.createQuery(
                            "SELECT e FROM Enrollment e WHERE e.studentId = :studentId",
                            Enrollment.class
                    )
                    .setParameter("studentId", studentId)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    public List<Enrollment> findByCourseId(Integer courseId) {
        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            return entityManager.createQuery(
                            "SELECT e FROM Enrollment e WHERE e.courseId = :courseId",
                            Enrollment.class
                    )
                    .setParameter("courseId", courseId)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    public List<Enrollment> findByAcademicGroupId(Integer academicGroupId) {
        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            return entityManager.createQuery(
                            "SELECT e FROM Enrollment e WHERE e.academicGroupId = :academicGroupId",
                            Enrollment.class
                    )
                    .setParameter("academicGroupId", academicGroupId)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    public void save(Enrollment enrollment) {
        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(enrollment);
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

    public void update(Enrollment enrollment) {
        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(enrollment);
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
        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Enrollment enrollment =
                    entityManager.find(Enrollment.class, id);

            if (enrollment != null) {
                entityManager.remove(enrollment);
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