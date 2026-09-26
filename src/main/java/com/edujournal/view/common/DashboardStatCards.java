package com.edujournal.view.common;

import com.edujournal.backend.service.AcademicGroupService;
import com.edujournal.backend.service.AssessmentsService;
import com.edujournal.backend.service.CourseService;
import com.edujournal.backend.service.EnrollmentService;
import com.edujournal.backend.service.StudentService;
import com.edujournal.backend.utils.UserSession;
import com.edujournal.dao.GradesDAO;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.Assessments;
import com.edujournal.entity.Enrollment;
import com.edujournal.entity.Grades;
import com.edujournal.entity.Role;
import com.edujournal.model.CourseDTO;
import com.edujournal.view.StatCard;
import com.edujournal.view.teacher.GradesTab;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DashboardStatCards {

    public static HBox build(Role role) {
        HBox box = new HBox(16);
        box.setAlignment(Pos.CENTER);

        switch (role) {
            case TEACHER -> {
                Integer teacherId = UserSession.getInstance().getCurrentUser().getId();
                List<CourseDTO> teacherCourses = new CourseService().findByUserId(teacherId);
                AssessmentsService assessmentsService = new AssessmentsService();
                EnrollmentService enrollmentService = new EnrollmentService();
                Set<Integer> studentIds = new HashSet<>();
                int assessments = 0;
                for (CourseDTO c : teacherCourses) {
                    assessments += assessmentsService.getByCourseId(c.getId()).size();
                    for (Enrollment e : enrollmentService.findByCourseId(c.getId()))
                        studentIds.add(e.getStudentId());
                }
                int groups = (int) teacherCourses.stream()
                        .map(CourseDTO::getGroupId).filter(Objects::nonNull).distinct().count();
                box.getChildren().addAll(
                        new StatCard("Courses",     String.valueOf(teacherCourses.size()), "/images/course_icon.png"),
                        new StatCard("Groups",      String.valueOf(groups),                "/images/group_icon.png"),
                        new StatCard("Students",    String.valueOf(studentIds.size()),     "/images/student_icon.png"),
                        new StatCard("Assessments", String.valueOf(assessments),           "/images/assessement_icon.png")
                );
            }
            case STUDENT -> {
                Integer userId = UserSession.getInstance().getCurrentUser().getId();
                com.edujournal.entity.Student student = new StudentService().findByUserId(userId);
                EnrollmentService enrollmentService = new EnrollmentService();
                AssessmentsService assessmentsService = new AssessmentsService();
                GradesDAO gradesDAO = new GradesDAO();

                int enrolledCourses = 0;
                int assessments = 0;
                int completed = 0;
                double gradeSum = 0;
                int gradeCount = 0;
                CourseService courseService = new CourseService();

                if (student != null) {
                    List<Enrollment> enrollments = enrollmentService.findByStudentId(student.getId());
                    enrolledCourses = enrollments.size();
                    for (Enrollment e : enrollments) {
                        List<Assessments> courseAssessments = assessmentsService.getByCourseId(e.getCourseId());
                        assessments += courseAssessments.size();
                        Map<Integer, Grades> gradeMap = gradesDAO.findByEnrollment(e.getId()).stream()
                                .collect(Collectors.toMap(Grades::getAssessmentId, g -> g));
                        String result = GradesTab.computeFinalGrade(courseAssessments, gradeMap, courseAssessments.size());
                        if (!result.equals("—")) {
                            gradeSum += Integer.parseInt(result.substring(0, 1));
                            gradeCount++;
                        }
                        CourseDTO course = courseService.getById(e.getCourseId());
                        if (course != null && course.getEndDate() != null && course.getEndDate().isBefore(LocalDate.now())) {
                            completed++;
                        }
                    }
                }

                String avgGrade = gradeCount == 0 ? "—" : String.format("%.1f", gradeSum / gradeCount);

                box.getChildren().addAll(
                        new StatCard("Average Grade",   avgGrade,                      "/images/av_grade_icon.png"),
                        new StatCard("Assessments",     String.valueOf(assessments),    "/images/assessement_icon.png"),
                        new StatCard("Current Courses", String.valueOf(enrolledCourses),"/images/current_course_icon.png"),
                        new StatCard("Completed",       String.valueOf(completed),      "/images/course_icon.png")
                );
            }
            case ADMINISTRATOR -> {
                int students = new StudentService().findAll().size();
                int teachers = (int) new UserDAO().findAll().stream()
                        .filter(u -> u.getRole() == Role.TEACHER).count();
                int courses  = new CourseService().findAll().size();
                int groups   = new AcademicGroupService().findAll().size();
                box.getChildren().addAll(
                        new StatCard("Students", String.valueOf(students), "/images/student_icon.png"),
                        new StatCard("Teachers", String.valueOf(teachers), "/images/teacher_icon.png"),
                        new StatCard("Courses",  String.valueOf(courses),  "/images/course_icon.png"),
                        new StatCard("Groups",   String.valueOf(groups),   "/images/group_icon.png")
                );
            }
        }

        return box;
    }

}
