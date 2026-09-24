package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.entity.Student;
import com.edujournal.entity.User;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AcademicGroupDAO {

    public AcademicGroup findById(Integer id) {
        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            return entityManager.find(AcademicGroup.class, id);
        } finally {
            entityManager.close();
        }
    }

    public List<AcademicGroup> findAll() {
        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            return entityManager.createQuery(
                    "SELECT a FROM AcademicGroup a",
                    AcademicGroup.class
            ).getResultList();
        } finally {
            entityManager.close();
        }
    }

    public AcademicGroup findByName(String name) {
        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            return entityManager.createQuery(
                            "SELECT a FROM AcademicGroup a WHERE a.name = :name",
                            AcademicGroup.class
                    )
                    .setParameter("name", name)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            entityManager.close();
        }
    }

    public void addStudentToGroup(Integer studentId, Integer groupId) {
        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Student student = entityManager.find(Student.class, studentId);

            if (student != null) {
                student.setAcademicGroupId(groupId);
                entityManager.merge(student);
            }

            entityManager.getTransaction().commit();

        } finally {
            entityManager.close();
        }
    }

    public void save(AcademicGroup academicGroup) {
        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

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

    public void update(AcademicGroup academicGroup) {
        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(academicGroup);
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

            AcademicGroup academicGroup =
                    entityManager.find(AcademicGroup.class, id);

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