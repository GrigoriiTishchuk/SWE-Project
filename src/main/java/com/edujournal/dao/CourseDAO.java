package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import com.edujournal.entity.Course;
import jakarta.persistence.EntityManager;

import java.util.List;

public class CourseDAO {

    public Course findById(Integer id) {

        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            return entityManager.find(Course.class, id);

        } finally {
            entityManager.close();
        }
    }

    public List<Course> findAll() {

        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            return entityManager.createQuery(
                    "SELECT c FROM Course c",
                    Course.class
            ).getResultList();

        } finally {
            entityManager.close();
        }
    }

    public Course findByName(String name) {

        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            return entityManager.createQuery(
                            "SELECT c FROM Course c WHERE c.name = :name",
                            Course.class
                    )
                    .setParameter("name", name)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

        } finally {
            entityManager.close();
        }
    }

    public Course findByCode(String code) {

        EntityManager entityManager =
                JPAUtil.getEntityManagerFactory()
                        .createEntityManager();

        try {
            return entityManager.createQuery(
                            "SELECT c FROM Course c WHERE c.code = :code",
                            Course.class
                    )
                    .setParameter("code", code)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

        } finally {
            entityManager.close();
        }
    }
}