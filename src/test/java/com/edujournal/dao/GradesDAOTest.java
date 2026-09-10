package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import com.edujournal.entity.Grades;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("Temporarily disabled")
class GradesDAOTest {
    EntityManager em;
    GradesDAO gdao;

    @BeforeEach
    void setUp() {
        em = JPAUtil.getEntityManagerFactory().createEntityManager();
        gdao = new GradesDAO();
    }

    @AfterEach
    void tearDown() {
        em.close();
    }

    @Test
    void findById_returnEntityIfExists() {
        Grades grade = new Grades();
        grade.setStudentId(1);
        grade.setAssessmentId(10);
        grade.setScore(8.5);

        em.getTransaction().begin();
        em.persist(grade);
        em.getTransaction().commit();

        Grades result = gdao.findById(grade.getId());

        assertNotNull(result);
        assertEquals(1, result.getStudentId());
        assertEquals(10, result.getAssessmentId());
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
        grade.setStudentId(2);
        grade.setAssessmentId(20);
        grade.setScore(7.0);

        gdao.save(grade);

        Grades result = gdao.findById(grade.getId());

        assertNotNull(result);
        assertEquals(2, result.getStudentId());
        assertEquals(20, result.getAssessmentId());
        assertEquals(7.0, result.getScore());
    }

    @Test
    void findById_returnNullAfterDelete() {
        Grades grade = new Grades();
        grade.setStudentId(3);
        grade.setAssessmentId(30);
        grade.setScore(9.0);

        gdao.save(grade);

        gdao.deleteById(grade.getId());

        Grades result = gdao.findById(grade.getId());

        assertNull(result);
    }

    @Test
    void findByStudent_returnListIfExists() {
        Grades g1 = new Grades();
        g1.setStudentId(1);
        g1.setAssessmentId(10);
        g1.setScore(8.0);

        Grades g2 = new Grades();
        g2.setStudentId(1);
        g2.setAssessmentId(11);
        g2.setScore(9.0);

        em.getTransaction().begin();
        em.persist(g1);
        em.persist(g2);
        em.getTransaction().commit();

        List<Grades> result = gdao.findByStudent(1);

        assertEquals(2, result.size());
    }

    @Test
    void findByStudent_returnEmptyListIfNotExist() {
        List<Grades> result = gdao.findByStudent(999);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByStudent_returnMultipleGrades() {
        Grades g1 = new Grades();
        g1.setStudentId(2);
        g1.setAssessmentId(20);
        g1.setScore(7.0);

        Grades g2 = new Grades();
        g2.setStudentId(2);
        g2.setAssessmentId(21);
        g2.setScore(6.5);

        Grades g3 = new Grades();
        g3.setStudentId(2);
        g3.setAssessmentId(22);
        g3.setScore(9.0);

        em.getTransaction().begin();
        em.persist(g1);
        em.persist(g2);
        em.persist(g3);
        em.getTransaction().commit();

        List<Grades> result = gdao.findByStudent(2);

        assertEquals(3, result.size());
    }

    @Test
    void findByStudent_shouldNotReturnOtherStudentsGrades() {
        Grades g1 = new Grades();
        g1.setStudentId(3);
        g1.setAssessmentId(30);
        g1.setScore(8.0);

        Grades g2 = new Grades();
        g2.setStudentId(4);
        g2.setAssessmentId(40);
        g2.setScore(9.0);

        em.getTransaction().begin();
        em.persist(g1);
        em.persist(g2);
        em.getTransaction().commit();

        List<Grades> result = gdao.findByStudent(3);

        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getStudentId());
    }

    @Test
    void findByAssessment_returnListIfExists() {
        Grades g1 = new Grades();
        g1.setStudentId(1);
        g1.setAssessmentId(10);
        g1.setScore(8.0);

        Grades g2 = new Grades();
        g2.setStudentId(2);
        g2.setAssessmentId(10);
        g2.setScore(9.0);

        em.getTransaction().begin();
        em.persist(g1);
        em.persist(g2);
        em.getTransaction().commit();

        List<Grades> result = gdao.findByAssessment(10);

        assertEquals(2, result.size());
    }

    @Test
    void findByAssessment_returnEmptyListIfNotExist() {
        List<Grades> result = gdao.findByAssessment(999);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByAssessment_returnMultipleGrades() {
        Grades g1 = new Grades();
        g1.setStudentId(1);
        g1.setAssessmentId(20);
        g1.setScore(7.0);

        Grades g2 = new Grades();
        g2.setStudentId(2);
        g2.setAssessmentId(20);
        g2.setScore(6.5);

        Grades g3 = new Grades();
        g3.setStudentId(3);
        g3.setAssessmentId(20);
        g3.setScore(9.0);

        em.getTransaction().begin();
        em.persist(g1);
        em.persist(g2);
        em.persist(g3);
        em.getTransaction().commit();

        List<Grades> result = gdao.findByAssessment(20);

        assertEquals(3, result.size());
    }

    @Test
    void findByAssessment_shouldNotReturnOtherAssessmentsGrades() {
        Grades g1 = new Grades();
        g1.setStudentId(3);
        g1.setAssessmentId(30);
        g1.setScore(8.0);

        Grades g2 = new Grades();
        g2.setStudentId(4);
        g2.setAssessmentId(40);
        g2.setScore(9.0);

        em.getTransaction().begin();
        em.persist(g1);
        em.persist(g2);
        em.getTransaction().commit();

        List<Grades> result = gdao.findByAssessment(30);

        assertEquals(1, result.size());
        assertEquals(30, result.get(0).getAssessmentId());
    }

    @Test
    void findByStudentAssessment_returnEntityIfExists() {
        Grades g = new Grades();
        g.setStudentId(1);
        g.setAssessmentId(10);
        g.setScore(8.0);

        em.getTransaction().begin();
        em.persist(g);
        em.getTransaction().commit();

        Grades result = gdao.findByStudentAssessment(1, 10);

        assertNotNull(result);
        assertEquals(1, result.getStudentId());
        assertEquals(10, result.getAssessmentId());
        assertEquals(8.0, result.getScore());
    }

    @Test
    void findByStudentAssessment_returnNullIfNotExist() {
        Grades result = gdao.findByStudentAssessment(999, 999);
        assertNull(result);
    }

    @Test
    void findByStudentAssessment_shouldNotReturnWrongPair() {
        Grades g1 = new Grades();
        g1.setStudentId(1);
        g1.setAssessmentId(10);
        g1.setScore(8.0);

        Grades g2 = new Grades();
        g2.setStudentId(2);
        g2.setAssessmentId(10);
        g2.setScore(9.0);

        em.getTransaction().begin();
        em.persist(g1);
        em.persist(g2);
        em.getTransaction().commit();

        Grades result = gdao.findByStudentAssessment(1, 10);

        assertNotNull(result);
        assertEquals(1, result.getStudentId());
        assertEquals(10, result.getAssessmentId());
    }

    @Test
    void findByStudentAssessment_returnFirstIfMultipleExist() {
        Grades g1 = new Grades();
        g1.setStudentId(3);
        g1.setAssessmentId(30);
        g1.setScore(7.0);

        Grades g2 = new Grades();
        g2.setStudentId(3);
        g2.setAssessmentId(30);
        g2.setScore(9.0);

        em.getTransaction().begin();
        em.persist(g1);
        em.persist(g2);
        em.getTransaction().commit();

        Grades result = gdao.findByStudentAssessment(3, 30);

        assertNotNull(result);
        assertEquals(3, result.getStudentId());
        assertEquals(30, result.getAssessmentId());
    }

    @Test
    void save_shouldPersistEntity() {
        Grades grade = new Grades();
        grade.setStudentId(1);
        grade.setAssessmentId(10);
        grade.setScore(8.0);

        gdao.save(grade);

        Grades result = gdao.findById(grade.getId());

        assertNotNull(result);
        assertEquals(1, result.getStudentId());
        assertEquals(10, result.getAssessmentId());
        assertEquals(8.0, result.getScore());
    }

    @Test
    void save_shouldGenerateId() {
        Grades grade = new Grades();
        grade.setStudentId(2);
        grade.setAssessmentId(20);
        grade.setScore(9.0);

        gdao.save(grade);

        assertTrue(grade.getId() > 0);
    }

    @Test
    void save_shouldRollbackOnError() {
        Grades grade = new Grades();
        grade.setStudentId(3);
        grade.setAssessmentId(30);
        grade.setScore(10.0);

        // create error
        em.close();

        assertThrows(Exception.class, () -> gdao.save(grade));

        EntityManager newEm = JPAUtil.getEntityManagerFactory().createEntityManager();
        Grades result = newEm.find(Grades.class, grade.getId());
        assertNull(result);
        newEm.close();
    }

    @Test
    void update_shouldModifyEntity() {
        Grades grade = new Grades();
        grade.setStudentId(1);
        grade.setAssessmentId(10);
        grade.setScore(8.0);

        gdao.save(grade);

        grade.setScore(9.5);
        gdao.update(grade);

        Grades result = gdao.findById(grade.getId());

        assertEquals(9.5, result.getScore());
    }

    @Test
    void update_shouldModifyMultipleFields() {
        Grades grade = new Grades();
        grade.setStudentId(2);
        grade.setAssessmentId(20);
        grade.setScore(7.0);

        gdao.save(grade);

        grade.setStudentId(3);
        grade.setAssessmentId(25);
        grade.setScore(10.0);

        gdao.update(grade);

        Grades result = gdao.findById(grade.getId());

        assertEquals(3, result.getStudentId());
        assertEquals(25, result.getAssessmentId());
        assertEquals(10.0, result.getScore());
    }

    @Test
    void update_shouldRollbackOnError() {
        Grades grade = new Grades();
        grade.setStudentId(5);
        grade.setAssessmentId(50);
        grade.setScore(7.0);

        gdao.save(grade);

        // create error
        em.close();

        assertThrows(Exception.class, () -> gdao.update(grade));

        EntityManager newEm = JPAUtil.getEntityManagerFactory().createEntityManager();
        Grades result = newEm.find(Grades.class, grade.getId());
        assertEquals(7.0, result.getScore());
        newEm.close();
    }

    @Test
    void deleteById_shouldDeleteEntity() {
        Grades grade = new Grades();
        grade.setStudentId(1);
        grade.setAssessmentId(10);
        grade.setScore(8.0);

        gdao.save(grade);

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
        grade.setStudentId(2);
        grade.setAssessmentId(20);
        grade.setScore(9.0);

        gdao.save(grade);

        gdao.deleteById(grade.getId());
        gdao.deleteById(grade.getId()); // повторное удаление

        Grades result = gdao.findById(grade.getId());
        assertNull(result);
    }

    @Test
    void deleteById_shouldRollbackOnError() {
        Grades grade = new Grades();
        grade.setStudentId(3);
        grade.setAssessmentId(30);
        grade.setScore(7.0);

        gdao.save(grade);

        // create error
        em.close();

        assertThrows(Exception.class, () -> gdao.deleteById(grade.getId()));

        EntityManager newEm = JPAUtil.getEntityManagerFactory().createEntityManager();
        Grades result = newEm.find(Grades.class, grade.getId());
        assertNotNull(result);
        newEm.close();
    }

}