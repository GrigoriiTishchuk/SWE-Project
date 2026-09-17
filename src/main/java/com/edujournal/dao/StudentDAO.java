package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import com.edujournal.entity.Student;
import jakarta.persistence.EntityManager;

import java.util.List;

public class StudentDAO {

    public Student findById(Integer id) {
        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            return entityManager.find(Student.class, id);
        } finally {
            entityManager.close();
        }
    }

    public List<Student> findAll() {
        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            return entityManager.createQuery(
                    "SELECT s FROM Student s",
                    Student.class
            ).getResultList();
        } finally {
            entityManager.close();
        }
    }

    public Student findByStudentNumber(String studentNumber) {
        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            return entityManager.createQuery(
                            "SELECT s FROM Student s WHERE s.studentNumber = :studentNumber",
                            Student.class
                    )
                    .setParameter("studentNumber", studentNumber)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            entityManager.close();
        }
    }

    public void save(Student student) {
        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(student);
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

    public void update(Student student) {
        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(student);
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

            Student student = entityManager.find(Student.class, id);

            if (student != null) {
                entityManager.remove(student);
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