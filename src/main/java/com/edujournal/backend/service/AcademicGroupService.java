
package com.edujournal.backend.service;

import com.edujournal.backend.utils.AcademicGroupMapper;
import com.edujournal.dao.AcademicGroupDAO;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.model.AcademicGroupDTO;
import com.edujournal.model.CourseDTO;

import java.util.List;

/**
 * Service class responsible for managing academic groups.
 * <p>
 * This class provides business logic for creating, retrieving, updating,
 * and deleting academic groups. It also handles student enrollment
 * operations when students are added to or removed from academic groups.
 * </p>
 *
 * @author EduJournal
 * @version 1.0
 */
public class AcademicGroupService {


    /** Data access object for academic group operations. */
    private final AcademicGroupDAO academicGroupDAO;

    /** Maps academic group entities to DTO objects. */
    private final AcademicGroupMapper agMapper =
            new AcademicGroupMapper();

    /** Service responsible for managing course operations. */
    private final CourseService courseService =
            new CourseService();

    /** Service responsible for managing student enrollments. */
    private final EnrollmentService enrollmentService =
            new EnrollmentService();


    /**
     * Creates a new AcademicGroupService instance and initializes
     * the AcademicGroupDAO.
     */
    public AcademicGroupService() {
        this.academicGroupDAO = new AcademicGroupDAO();
    }

    /**
     * Retrieves an academic group by its unique identifier.
     *
     * @param id the unique identifier of the academic group
     * @return the academic group if found, or null if no group exists
     *         with the given identifier
     */
    public AcademicGroup findById(final Integer id) {
        return academicGroupDAO.findById(id);
    }

    /**
     * Retrieves all academic groups.
     *
     * @return a list containing all academic groups
     */
    public List<AcademicGroup> findAll() {
        return academicGroupDAO.findAll();
    }

    /**
     * Retrieves all academic groups and converts them into DTO objects.
     *
     * @return a list of AcademicGroupDTO objects representing all
     * academic groups
     */
    public List<AcademicGroupDTO> findAllDTO() {
        return academicGroupDAO.findAll().stream()
                .map(agMapper::toDTO).toList();
    }

    /**
     * Retrieves an academic group by its name.
     *
     * @param name the name of the academic group
     * @return the academic group if found, or null if no matching group exists
     */
    public AcademicGroup findByName(final String name) {
        return academicGroupDAO.findByName(name);
    }

    /**
     * Saves a new academic group to the database.
     *
     * @param academicGroup the academic group to be saved
     */
    public void save(final AcademicGroup academicGroup) {
        academicGroupDAO.save(academicGroup);
    }

    /**
     * Updates the information of an existing academic group.
     *
     * @param academicGroup the academic group containing the updated
     * information
     */
    public void update(final AcademicGroup academicGroup) {
        academicGroupDAO.update(academicGroup);
    }

    /**
     * Deletes an academic group using its unique identifier.
     *
     * @param id the unique identifier of the academic group to be deleted
     */
    public void delete(final Integer id) {
        academicGroupDAO.delete(id);
    }

    /**
     * Checks whether an academic group with the specified name exists.
     *
     * @param name the name to be checked
     * @return true if an academic group with the given name exists;
     *         false otherwise
     */
    public boolean existsByName(final String name) {
        return academicGroupDAO.findByName(name) != null;
    }

    /**
     * Adds a student to an academic group.
     * <p>
     * After adding the student to the group, this method retrieves all
     * courses associated with that group and automatically creates
     * enrollments for the student in those courses if they do not
     * already exist.
     * </p>
     *
     * @param studentId the unique identifier of the student
     * @param groupId the unique identifier of the academic group
     */
    public void addStudentToGroup(final Integer studentId,
                                  final Integer groupId) {
        academicGroupDAO.addStudentToGroup(studentId, groupId);
        List<CourseDTO> courses = courseService.findByGroupId(groupId);

        for (CourseDTO course : courses) {
            enrollmentService.createIfNotExists(
                    studentId,
                    course.getId(),
                    groupId
            );
        }
    }

    /**
     * Removes a student from an academic group.
     * <p>
     * After removing the student from the group, this method also deletes
     * all course enrollments associated with that student and academic group.
     * </p>
     *
     * @param studentId the unique identifier of the student
     * @param groupId the unique identifier of the academic group
     */
    public void removeStudentFromGroup(final Integer studentId,
                                       final Integer groupId) {

        academicGroupDAO.removeStudentFromGroup(
                studentId,
                groupId
        );

        enrollmentService.deleteByStudentAndAcademicGroup(
                studentId,
                groupId
        );
    }
}
