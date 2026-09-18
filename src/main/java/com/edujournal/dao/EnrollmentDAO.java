package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import com.edujournal.entity.Enrollment;
import jakarta.persistence.EntityManager;

import java.util.List;

public class EnrollmentDAO {

    public List<Enrollment> findByCourseAndGroup(int courseId, int groupId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT e FROM Enrollment e WHERE e.courseId = :courseId AND e.academicGroupId = :groupId",
                    Enrollment.class)
                    .setParameter("courseId", courseId)
                    .setParameter("groupId", groupId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
