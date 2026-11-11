package com.edujournal.view.controller;

import com.edujournal.backend.service.AcademicGroupService;
import com.edujournal.backend.service.CourseService;
import com.edujournal.backend.service.UserService;
import com.edujournal.backend.utils.UserSession;
import com.edujournal.backend.utils.CourseMapper;
import com.edujournal.config.LocalizationManager;
import com.edujournal.entity.Course;
import com.edujournal.entity.Role;
import com.edujournal.model.AcademicGroupDTO;
import com.edujournal.model.CourseDTO;
import com.edujournal.model.UserDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

/**
 * Controller responsible for managing courses.
 * Supports course creation, editing, deletion, filtering and role-based actions.
 */
public class CourseController extends BaseController<CourseDTO> {

    private final CourseService courseService = new CourseService();
    private final CourseMapper courseMapper = new CourseMapper();
    private final UserService userService = new UserService();
    private final AcademicGroupService academicGroupService = new AcademicGroupService();

    private static final String DEFAULT_YEAR = "2026/2027";
    private final Role role;
    private String selectedYear = DEFAULT_YEAR;

    /**
     * Creates the course controller.
     *
     * @param role current user role
     */
    public CourseController(Role role) {
        this.role = role;
        configureColumns();
        setupRoleButtons();
        loadAndShowItems();
    }

    /**
     * Updates the selected academic year and refreshes displayed courses.
     *
     * @param year selected academic year
     */
    public void setYear(String year) {
        this.selectedYear = year;
        applyFilter();
    }

    /**
     * Configures button visibility and actions according to the current user role.
     */
    private void setupRoleButtons() {
        if (role == Role.ADMINISTRATOR) {
            addBtn.setVisible(true);
            editBtn.setVisible(true);
            deleteBtn.setVisible(true);
            viewBtn.setVisible(true);
        } else if (role == Role.TEACHER) {
            addBtn.setVisible(false);
            editBtn.setVisible(false);
            deleteBtn.setVisible(false);
            viewBtn.setText(LocalizationManager.get("addAssessment"));
            viewBtn.setOnAction(e -> openAddAssessmentPage());
        }
    }

    /**
     * Loads all courses available to the current user.
     *
     * @return list of courses
     */
    @Override
    protected List<CourseDTO> loadAllItems() {
        List<CourseDTO> all = (role == Role.TEACHER)
                ? courseService.findByUserId(UserSession.getInstance().getCurrentUser().getId())
                : courseService.findAll();
        return all.stream()
                .filter(c -> selectedYear.equals(c.getAcademicYear()))
                .toList();
    }

    /**
     * Checks whether a course matches the selected filter and search text.
     *
     * @param course course to evaluate
     * @param filter selected filter
     * @param searchText search text
     * @return true if the course matches
     */
    @Override
    protected boolean matchesFilter(CourseDTO course, String filter, String searchText) {
        searchText = searchText.toLowerCase();

        switch (filter) {
            case "ID":
                return String.valueOf(course.getId()).contains(searchText);

            case "Name":
                return course.getName() != null &&
                        course.getName().toLowerCase().contains(searchText);

            case "Code":
                return course.getCode() != null &&
                        course.getCode().toLowerCase().contains(searchText);

            case "Teacher":
                return course.getTeacherName() != null &&
                        course.getTeacherName().toLowerCase().contains(searchText);

            case "Academic Group":
                return course.getGroupName() != null &&
                        course.getGroupName().toLowerCase().contains(searchText);

            case "All":
            default:
                return matchesAllFields(
                        List.of(
                                String.valueOf(course.getId()),
                                safe(course.getName()),
                                safe(course.getCode()),
                                safe(course.getTeacherName()),
                                safe(course.getGroupName())
                        ),
                        searchText
                );
        }
    }

    /**
     * Configures course table columns and filter options.
     */
    @Override
    protected void configureColumns() {
        TableColumn<CourseDTO, String> idCol = new TableColumn<>(LocalizationManager.get("id"));
        idCol.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getId())));

        TableColumn<CourseDTO, String> codeCol = new TableColumn<>(LocalizationManager.get("code"));
        codeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCode()));

        TableColumn<CourseDTO, String> nameCol = new TableColumn<>(LocalizationManager.get("name"));
        nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));

        TableColumn<CourseDTO, String> teacherCol = new TableColumn<>(LocalizationManager.get("teacher"));
        teacherCol.setCellValueFactory(c ->
                new SimpleStringProperty(safe(c.getValue().getTeacherName()))
        );

        TableColumn<CourseDTO, String> groupCol = new TableColumn<>(LocalizationManager.get("academicGroup"));
        groupCol.setCellValueFactory(c ->
                new SimpleStringProperty(safe(c.getValue().getGroupName()))
        );

        table.getColumns().addAll(idCol, codeCol, nameCol, teacherCol, groupCol);

        filterCombo.getItems().addAll("All", "ID", "Name", "Code", "Teacher", "Academic Group");
        filterCombo.setValue("All");

        Map<String, String> filterKeyMap = Map.of(
                "All", "all", "ID", "id", "Name", "name", "Code", "code",
                "Teacher", "teacher", "Academic Group", "academicGroup"
        );
        filterCombo.setCellFactory(list -> createLocalizedCell(filterKeyMap));
        filterCombo.setButtonCell(createLocalizedCell(filterKeyMap));
    }

    /**
     * Opens a dialog for creating a new course.
     */
    @Override
    public void showAddDialog() {
        Dialog<CourseDTO> dialog = new Dialog<>();
        dialog.setTitle(LocalizationManager.get("addCourse"));

        TextField nameField = new TextField();
        nameField.setPromptText(LocalizationManager.get("name"));

        TextField codeField = new TextField();
        codeField.setPromptText(LocalizationManager.get("code"));

        // Teacher ComboBox
        List<UserDTO> teachers = userService.findAllTeachers();
        ComboBox<UserDTO> teacherCombo = createComboBox(
                teachers,
                null,
                LocalizationManager.get("selectTeacher"),
                LocalizationManager.get("noTeacher2"),
                u -> u.getFirstName() + " " + u.getLastName()
        );

        // Group ComboBox
        List<AcademicGroupDTO> groups = academicGroupService.findAllDTO();
        ComboBox<AcademicGroupDTO> groupCombo = createComboBox(
                groups,
                null,
                LocalizationManager.get("selectGroup"),
                LocalizationManager.get("noGroup2"),
                AcademicGroupDTO::getName
        );

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText(LocalizationManager.get("startDate"));
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText(LocalizationManager.get("endDate"));

        VBox box = new VBox(10, nameField, codeField, teacherCombo, groupCombo, startDatePicker, endDatePicker);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn = new ButtonType(LocalizationManager.get("save"), ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == saveBtn) {

                String newName = nameField.getText().trim();
                String newCode = codeField.getText().trim();

                if (!validateCourseData(newName, newCode, newCode)) {
                    return null;
                }

                CourseDTO dto = new CourseDTO();
                dto.setName(nameField.getText());
                dto.setCode(codeField.getText());

                UserDTO selectedTeacher = teacherCombo.getValue();
                dto.setUserId(selectedTeacher == null ? null : selectedTeacher.getId());

                AcademicGroupDTO selectedGroup = groupCombo.getValue();
                dto.setGroupId(selectedGroup == null ? null : selectedGroup.getId());

                dto.setStartDate(startDatePicker.getValue());
                dto.setEndDate(endDatePicker.getValue());

                return dto;
            }
            return null;
        });

        CourseDTO result = dialog.showAndWait().orElse(null);

        if (result != null) {
            Course entity = courseMapper.toEntity(result);
            courseService.save(entity);
            loadAndShowItems();
        }
    }

    /**
     * Opens a dialog for editing the selected course.
     */
    @Override
    protected void showEditDialog() {
        CourseDTO course = table.getSelectionModel().getSelectedItem();
        if (course == null) {
            return;
        }

        Dialog<CourseDTO> dialog = new Dialog<>();
        dialog.setTitle(LocalizationManager.get("editCourse"));

        TextField nameField = new TextField(course.getName());
        TextField codeField = new TextField(course.getCode());

        // Teacher ComboBox
        List<UserDTO> teachers = userService.findAllTeachers();
        UserDTO selectedTeacher = teachers.stream()
                .filter(t -> t.getId().equals(course.getUserId()))
                .findFirst()
                .orElse(null);

        ComboBox<UserDTO> teacherCombo = createComboBox(
                teachers,
                selectedTeacher,
                LocalizationManager.get("selectTeacher"),
                LocalizationManager.get("noTeacher2"),
                u -> u.getFirstName() + " " + u.getLastName()
        );

        // Group ComboBox
        List<AcademicGroupDTO> groups = academicGroupService.findAllDTO();
        AcademicGroupDTO selectedGroup = groups.stream()
                .filter(g -> g.getId().equals(course.getGroupId()))
                .findFirst()
                .orElse(null);

        ComboBox<AcademicGroupDTO> groupCombo = createComboBox(
                groups,
                selectedGroup,
                LocalizationManager.get("selectGroup"),
                LocalizationManager.get("noGroup2"),
                AcademicGroupDTO::getName
        );

        DatePicker startDatePicker = new DatePicker(course.getStartDate());
        DatePicker endDatePicker = new DatePicker(course.getEndDate());

        VBox box = new VBox(10, nameField, codeField, teacherCombo, groupCombo, startDatePicker, endDatePicker);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn = new ButtonType(LocalizationManager.get("save"), ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                String newName = nameField.getText().trim();
                String newCode = codeField.getText().trim();

                if (!validateCourseData(newName, newCode, course.getCode())) {
                    return null;
                }

                course.setName(nameField.getText());
                course.setCode(codeField.getText());

                UserDTO t = teacherCombo.getValue();
                course.setUserId(t == null ? null : t.getId());

                AcademicGroupDTO g = groupCombo.getValue();
                course.setGroupId(g == null ? null : g.getId());

                course.setStartDate(startDatePicker.getValue());
                course.setEndDate(endDatePicker.getValue());

                return course;
            }
            return null;
        });

        CourseDTO updated = dialog.showAndWait().orElse(null);

        if (updated != null) {
            Course entity = courseMapper.toEntity(updated);
            courseService.update(entity);
            loadAndShowItems();
        }
    }

    /**
     * Validates course name and code before saving.
     *
     * @param newName new course name
     * @param newCode new course code
     * @param currentCode current course code
     * @return true if validation succeeds, false otherwise
     */
    private boolean validateCourseData(
            String newName,
            String newCode,
            String currentCode
    ) {
        if (newName.isBlank()) {
            showError(LocalizationManager.get("courseNameEmpty"));
            return false;
        }

        if (newCode.isBlank()) {
            showError(LocalizationManager.get("courseCodeEmpty"));
            return false;
        }

        if (!newCode.equalsIgnoreCase(currentCode)
                && courseService.existsByCode(newCode)) {
            showError(LocalizationManager.get("courseExists"));
            return false;
        }

        return true;
    }

    /**
     * Opens a confirmation dialog and deletes the selected course if confirmed.
     */
    @Override
    protected void showDeleteDialog() {
        CourseDTO course = table.getSelectionModel().getSelectedItem();
        if (course == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(LocalizationManager.get("deleteCourse"));
        alert.setHeaderText(LocalizationManager.get("sureToDelete") + " \"" + course.getName() + "\"?");
        alert.setContentText(LocalizationManager.get("undone"));

        ButtonType yes = new ButtonType(LocalizationManager.get("yes"), ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType(LocalizationManager.get("no"), ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yes, no);

        if (alert.showAndWait().orElse(no) == yes) {
            courseService.delete(course.getId());
            loadAndShowItems();
        }
    }

    /**
     * Opens the assessment page for the selected course.
     */
    private void openAddAssessmentPage() {
        CourseDTO course = table.getSelectionModel().getSelectedItem();
        if (course == null) {
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING, LocalizationManager.get("selectCourseFirst")).showAndWait();
            return;
        }
        com.edujournal.Main.showPage(new com.edujournal.view.teacher.TeacherGradebookPage(1, course.getName()));
    }

    /**
     * Opens role-specific course actions for the selected course.
     */
    @Override
    protected void onView() {
        CourseDTO selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        if (role == Role.ADMINISTRATOR) {
            showCourseInfoDialog(selected);
        } else if (role == Role.TEACHER) {
            showAssessmentDialog(selected);
        }
    }

    /**
     * Displays detailed information about the selected course.
     *
     * @param course selected course
     */
    private void showCourseInfoDialog(CourseDTO course) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(LocalizationManager.get("courseInfo"));
        alert.setHeaderText(course.getName());
        alert.setContentText(
                LocalizationManager.get("code") + ": " + course.getCode() + "\n" +
                LocalizationManager.get("teacher") + ": " + course.getTeacherName() + "\n" +
                LocalizationManager.get("academicGroup") + ": " + course.getGroupName());
        alert.showAndWait();
    }

    /**
     * Opens the teacher assessment view for the selected course.
     *
     * @param course selected course
     */
    private void showAssessmentDialog(CourseDTO course) {
        com.edujournal.Main.showPage(new com.edujournal.view.teacher.TeacherGradebookPage(1, course.getName()));
    }



}
