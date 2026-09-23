package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import com.edujournal.entity.Grades;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GradesDAOTest {
    EntityManager em;
    GradesDAO gdao;

    private final List<Grades> createdGrades = new ArrayList<>();
    @BeforeEach
    void setUp() {
        em = JPAUtil.getEntityManagerFactory().createEntityManager();
        gdao = new GradesDAO();
    }

    @AfterEach
    void cleanup() {
        for (Grades grade : createdGrades) {
            try {
                gdao.deleteById(grade.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        createdGrades.clear();

        if (em.isOpen()) {
            em.close();
        }
    }

    @Test
    void findById_returnEntityIfExists() {
        Grades grade = new Grades();
        grade.setEnrollmentId(1);
        grade.setAssessmentId(1);
        grade.setScore(8.5);

        em.getTransaction().begin();
        em.persist(grade);
        em.getTransaction().commit();

        createdGrades.add(grade);

        Grades result = gdao.findById(grade.getId());

        assertNotNull(result);
        assertEquals(1, result.getEnrollmentId());
        assertEquals(1, result.getAssessmentId());
        assertEquals(8.5, result.getScore());
    }

    @Test
    void findById_returnNullIfNotExist() {
        Grades result = gdao.findById(9999);
        assertNull(result);;
    }

    @Test
    void findById_returnSavedEntityAfterSave() {
        Grades grade = new Grades();
        grade.setEnrollmentId(2);
        grade.setAssessmentId(1);
        grade.setScore(7.0);

        gdao.save(grade);
        createdGrades.add(grade);

        Grades result = gdao.findById(grade.getId());

        assertNotNull(result);
        assertEquals(2, result.getEnrollmentId());
        assertEquals(1, result.getAssessmentId());
        assertEquals(7.0, result.getScore());
    }

    @Test
    void findById_returnNullAfterDelete() {
        Grades grade = new Grades();
        grade.setEnrollmentId(3);
        grade.setAssessmentId(1);
        grade.setScore(9.0);

        gdao.save(grade);
        createdGrades.add(grade);

        gdao.deleteById(grade.getId());

        Grades result = gdao.findById(grade.getId());

        assertNull(result);
    }
/*
    @Test
    void findByStudent_returnListIfExists() {
        Grades g1 = new Grades();
        g1.setEnrollmentId(1);
        g1.setAssessmentId(1);
        g1.setScore(8.0);

        Grades g2 = new Grades();
        g2.setEnrollmentId(1);
        g2.setAssessmentId(2);
        g2.setScore(9.0);

        em.getTransaction().begin();
        em.persist(g1);
        em.persist(g2);
        em.getTransaction().commit();

        createdGrades.add(g1);
        createdGrades.add(g2);
        List<Grades> result = gdao.findByEnrollment(1);

        assertEquals(2, result.size());
    }
*/
    @Test
    void findByStudent_returnEmptyListIfNotExist() {
        List<Grades> result = gdao.findByEnrollment(999);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByStudent_returnMultipleGrades() {
        Grades g1 = new Grades();
        g1.setEnrollmentId(2);
        g1.setAssessmentId(2);
        g1.setScore(7.0);

        Grades g2 = new Grades();
        g2.setEnrollmentId(2);
        g2.setAssessmentId(2);
        g2.setScore(6.5);

        Grades g3 = new Grades();
        g3.setEnrollmentId(1);
        g3.setAssessmentId(3);
        g3.setScore(9.0);

        em.getTransaction().begin();
        em.persist(g1);
        em.persist(g2);
        em.persist(g3);
        em.getTransaction().commit();
        createdGrades.add(g1);
        createdGrades.add(g2);
        createdGrades.add(g3);

        List<Grades> result = gdao.findByEnrollment(2);

        assertEquals(3, result.size());
    }

 /*
    @Test
    void findByStudent_shouldNotReturnOtherStudentsGrades() {
        Grades g1 = new Grades();
        g1.setEnrollmentId(3);
        g1.setAssessmentId(1);
        g1.setScore(8.0);

        Grades g2 = new Grades();
        g2.setEnrollmentId(4);
        g2.setAssessmentId(1);
        g2.setScore(9.0);

        em.getTransaction().begin();
        em.persist(g1);
        em.persist(g2);
        em.getTransaction().commit();
        createdGrades.add(g1);
        createdGrades.add(g2);


        List<Grades> result = gdao.findByEnrollment(3);

        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getEnrollmentId());
    }

    @Test
    void findByAssessment_returnListIfExists() {
        Grades g1 = new Grades();
        g1.setEnrollmentId(1);
        g1.setAssessmentId(1);
        g1.setScore(8.0);

        Grades g2 = new Grades();
        g2.setEnrollmentId(2);
        g2.setAssessmentId(1);
        g2.setScore(9.0);

        em.getTransaction().begin();
        em.persist(g1);
        em.persist(g2);
        em.getTransaction().commit();
        createdGrades.add(g1);
        createdGrades.add(g2);

        List<Grades> result = gdao.findByAssessment(10);

        assertEquals(2, result.size());
    }
*/
    @Test
    void findByAssessment_returnEmptyListIfNotExist() {
        List<Grades> result = gdao.findByAssessment(999);
        assertTrue(result.isEmpty());
    }
/*
    @Test
    void findByAssessment_returnMultipleGrades() {
        Grades g1 = new Grades();
        g1.setEnrollmentId(1);
        g1.setAssessmentId(1);
        g1.setScore(7.0);

        Grades g2 = new Grades();
        g2.setEnrollmentId(2);
        g2.setAssessmentId(1);
        g2.setScore(6.5);

        Grades g3 = new Grades();
        g3.setEnrollmentId(3);
        g3.setAssessmentId(1);
        g3.setScore(9.0);

        em.getTransaction().begin();
        em.persist(g1);
        em.persist(g2);
        em.persist(g3);
        em.getTransaction().commit();

        createdGrades.add(g1);
        createdGrades.add(g2);
        createdGrades.add(g3);

        List<Grades> result = gdao.findByAssessment(1);

        assertEquals(3, result.size());
    }

    @Test
    void findByAssessment_shouldNotReturnOtherAssessmentsGrades() {
        Grades g1 = new Grades();
        g1.setEnrollmentId(3);
        g1.setAssessmentId(1);
        g1.setScore(8.0);

        Grades g2 = new Grades();
        g2.setEnrollmentId(4);
        g2.setAssessmentId(1);
        g2.setScore(9.0);

        em.getTransaction().begin();
        em.persist(g1);
        em.persist(g2);
        em.getTransaction().commit();

        createdGrades.add(g1);
        createdGrades.add(g2);


        List<Grades> result = gdao.findByAssessment(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getAssessmentId());
    }

    @Test
    void findByStudentAssessment_returnEntityIfExists() {
        Grades g = new Grades();
        g.setEnrollmentId(1);
        g.setAssessmentId(1);
        g.setScore(8.0);

        em.getTransaction().begin();
        em.persist(g);
        em.getTransaction().commit();
        createdGrades.add(g);

        Grades result = gdao.findByEnrollmentAssessment(1, 1);

        assertNotNull(result);
        assertEquals(1, result.getEnrollmentId());
        assertEquals(10, result.getAssessmentId());
        assertEquals(8.0, result.getScore());
    }
*/
    @Test
    void findByStudentAssessment_returnNullIfNotExist() {
        Grades result = gdao.findByEnrollmentAssessment(999, 999);
        assertNull(result);
    }

    @Test
    void findByStudentAssessment_shouldNotReturnWrongPair() {
        Grades g1 = new Grades();
        g1.setEnrollmentId(1);
        g1.setAssessmentId(1);
        g1.setScore(8.0);

        Grades g2 = new Grades();
        g2.setEnrollmentId(2);
        g2.setAssessmentId(1);
        g2.setScore(9.0);

        em.getTransaction().begin();
        em.persist(g1);
        em.persist(g2);
        em.getTransaction().commit();

        createdGrades.add(g1);
        createdGrades.add(g2);

        Grades result = gdao.findByEnrollmentAssessment(1, 1);

        assertNotNull(result);
        assertEquals(1, result.getEnrollmentId());
        assertEquals(1, result.getAssessmentId());
    }

    @Test
    void findByStudentAssessment_returnFirstIfMultipleExist() {
        Grades g1 = new Grades();
        g1.setEnrollmentId(3);
        g1.setAssessmentId(1);
        g1.setScore(7.0);

        Grades g2 = new Grades();
        g2.setEnrollmentId(3);
        g2.setAssessmentId(1);
        g2.setScore(9.0);

        em.getTransaction().begin();
        em.persist(g1);
        em.persist(g2);
        em.getTransaction().commit();

        createdGrades.add(g1);
        createdGrades.add(g2);

        Grades result = gdao.findByEnrollmentAssessment(3, 1);

        assertNotNull(result);
        assertEquals(3, result.getEnrollmentId());
        assertEquals(1, result.getAssessmentId());
    }

    @Test
    void save_shouldPersistEntity() {
        Grades grade = new Grades();
        grade.setEnrollmentId(1);
        grade.setAssessmentId(1);
        grade.setScore(8.0);

        gdao.save(grade);
        createdGrades.add(grade);

        Grades result = gdao.findById(grade.getId());

        assertNotNull(result);
        assertEquals(1, result.getEnrollmentId());
        assertEquals(1, result.getAssessmentId());
        assertEquals(8.0, result.getScore());
    }

    @Test
    void save_shouldGenerateId() {
        Grades grade = new Grades();
        grade.setEnrollmentId(2);
        grade.setAssessmentId(1);
        grade.setScore(9.0);

        gdao.save(grade);
        createdGrades.add(grade);

        assertTrue(grade.getId() > 0);
    }
/*
    @Test
    void save_shouldRollbackOnError() {
        Grades grade = new Grades();
        grade.setEnrollmentId(999999);
        grade.setAssessmentId(999999);
        grade.setScore(10.0);

        assertThrows(Exception.class, () -> gdao.save(grade));

        assertEquals(0, grade.getId());
    }
*/
    @Test
    void update_shouldModifyEntity() {
        Grades grade = new Grades();
        grade.setEnrollmentId(1);
        grade.setAssessmentId(1);
        grade.setScore(8.0);

        gdao.save(grade);
        createdGrades.add(grade);

        grade.setScore(9.5);
        gdao.update(grade);

        Grades result = gdao.findById(grade.getId());

        assertEquals(9.5, result.getScore());
    }

    @Test
    void update_shouldModifyMultipleFields() {
        Grades grade = new Grades();
        grade.setEnrollmentId(2);
        grade.setAssessmentId(1);
        grade.setScore(7.0);

        gdao.save(grade);
        createdGrades.add(grade);

        grade.setEnrollmentId(3);
        grade.setAssessmentId(2);
        grade.setScore(10.0);

        gdao.update(grade);

        Grades result = gdao.findById(grade.getId());

        assertEquals(3, result.getEnrollmentId());
        assertEquals(2, result.getAssessmentId());
        assertEquals(10.0, result.getScore());
    }
/*
    @Test
    void update_shouldRollbackOnError() {
        Grades grade = new Grades();
        grade.setEnrollmentId(4);
        grade.setAssessmentId(1);
        grade.setScore(7.0);

        gdao.save(grade);
        createdGrades.add(grade);
        // create error
        em.close();

        assertThrows(Exception.class, () -> gdao.update(grade));

        EntityManager newEm = JPAUtil.getEntityManagerFactory().createEntityManager();
        Grades result = newEm.find(Grades.class, grade.getId());
        assertEquals(7.0, result.getScore());
        newEm.close();
    }
*/
    @Test
    void deleteById_shouldDeleteEntity() {
        Grades grade = new Grades();
        grade.setEnrollmentId(1);
        grade.setAssessmentId(1);
        grade.setScore(8.0);

        gdao.save(grade);
        createdGrades.add(grade);
        gdao.deleteById(grade.getId());

        Grades result = gdao.findById(grade.getId());
        assertNull(result);
    }

    @Test
    void deleteById_nonExisting_shouldDoNothing() {
        gdao.deleteById(999);
        assertTrue(true);
    }

    @Test
    void deleteById_twice_shouldNotFail() {
        Grades grade = new Grades();
        grade.setEnrollmentId(2);
        grade.setAssessmentId(1);
        grade.setScore(9.0);

        gdao.save(grade);
        createdGrades.add(grade);
        gdao.deleteById(grade.getId());
        gdao.deleteById(grade.getId()); // повторное удаление

        Grades result = gdao.findById(grade.getId());
        assertNull(result);
    }
/*
    @Test
    void deleteById_shouldRollbackOnError() {
        Grades grade = new Grades();
        grade.setEnrollmentId(3);
        grade.setAssessmentId(1);
        grade.setScore(7.0);

        gdao.save(grade);
        createdGrades.add(grade);
        // create error
        em.close();

        assertThrows(Exception.class, () -> gdao.deleteById(grade.getId()));

        EntityManager newEm = JPAUtil.getEntityManagerFactory().createEntityManager();
        Grades result = newEm.find(Grades.class, grade.getId());
        assertNotNull(result);
        newEm.close();
    }
*/
}