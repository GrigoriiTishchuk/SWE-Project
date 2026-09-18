package com.edujournal.view.controller;

import com.edujournal.backend.service.CourseService;
import com.edujournal.backend.utils.CourseMapper;
import com.edujournal.entity.Course;
import com.edujournal.entity.Role;
import com.edujournal.model.CourseDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class CourseController extends BorderPane {

    private final CourseService courseService = new CourseService();
    private final CourseMapper courseMapper = new CourseMapper();

    private final Role role;

    private TableView<CourseDTO> table;

    private TextField searchField;
    private ComboBox<String> filterComboBox;

    private ObservableList<CourseDTO> courses;
    private FilteredList<CourseDTO> filteredCourses;

    public CourseController(Role role) {
        this.role = role;
        buildUI();
        loadCourses();
    }

    private HBox buildFilterBar() {
        searchField = new TextField();
        searchField.setPromptText("Search courses...");
        searchField.setPrefWidth(250);
        filterComboBox = new ComboBox<>();

        filterComboBox.getItems().addAll(
                "All",
                "ID",
                "Name",
                "Code"
                // "Teacher ID"
        );

        filterComboBox.setValue("All");
        Button refreshButton = new Button("Refresh");
        Button clearButton = new Button("Clear");
        HBox filterBar = new HBox(10);
        filterBar.setPadding(new Insets(10, 0, 10, 0));
        filterBar.getChildren().addAll(
                new Label("Search:"),
                searchField,
                new Label("Filter by:"),
                filterComboBox,
                clearButton,
                refreshButton
        );

        searchField.textProperty().addListener( (observable, oldValue, newValue) -> applyFilter() );
        filterComboBox.valueProperty().addListener( (observable, oldValue, newValue) -> applyFilter() );
        clearButton.setOnAction(event -> {
            searchField.clear();
            filterComboBox.setValue("All");
        });

        refreshButton.setOnAction(event -> loadCourses());

        return filterBar;
    }

    private void buildUI() {
        table = new TableView<>();

        TableColumn<CourseDTO, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getId())));
        idCol.setSortable(true);

        TableColumn<CourseDTO, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCode()));
        idCol.setSortable(true);

        TableColumn<CourseDTO, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        idCol.setSortable(true);

        /* TODO: uncomment, when Teacher appears
        TableColumn<CourseDTO, String> teacherCol = new TableColumn<>("Teacher ID");
        teacherCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUserId() == null ? "" : String.valueOf(c.getValue().getUserId())));
        idCol.setSortable(true);
        */

        table.getColumns().addAll(
                idCol,
                codeCol,
                nameCol
                //teacherCol
        );

        // Double click opens information about the course
        table.setRowFactory(tv -> {
            TableRow<CourseDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    CourseDTO course = row.getItem();
                    showCourseInfoDialog(course);
                }
            });
            return row;
        });

        Button addBtn = new Button("Add");
        Button editBtn = new Button("Edit");
        Button deleteBtn = new Button("Delete");
        Button addAssessmentBtn = new Button("Add Assessment");

        addBtn.setOnAction(e -> showAddCourseDialog());

        editBtn.setOnAction(e -> {
            CourseDTO selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showEditCourseDialog(selected);
        });

        deleteBtn.setOnAction(e -> {
            CourseDTO selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showDeleteDialog(selected);
        });

        addAssessmentBtn.setOnAction(e -> {
            CourseDTO selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showAssessmentDialog(selected);
            }
        });

        HBox actions = new HBox(10);
        actions.setPadding(new Insets(10));

        // Buttons available by roles
        if (role == Role.ADMINISTRATOR) {
            actions.getChildren().addAll(addBtn, editBtn, deleteBtn);
        } else if (role == Role.TEACHER) {
            actions.getChildren().add(addAssessmentBtn);
        }

        HBox filterBar = buildFilterBar();

        VBox layout = new VBox(10, filterBar, table, actions);
        layout.setPadding(new Insets(10));

        setCenter(layout);
    }

    private void loadCourses() {
        List<CourseDTO> result = courseService.findAll();
        courses = FXCollections.observableArrayList(result);
        filteredCourses = new FilteredList<>( courses, course -> true );
        table.setItems(filteredCourses);
        applyFilter();
    }

    private void applyFilter() {

        if (filteredCourses == null) {
            return;
        }

        String searchText = searchField.getText().trim().toLowerCase();
        String selectedFilter = filterComboBox.getValue();

        if (selectedFilter == null) {
            selectedFilter = "All";
        }

        final String filter = selectedFilter;

        filteredCourses.setPredicate(course -> {

            // Empty search -> show all courses
            if (searchText.isEmpty()) {
                return true;
            }

            switch (filter) {
                case "ID":
                    return String.valueOf(course.getId()).contains(searchText);

                case "Name":
                    return course.getName() != null && course.getName().toLowerCase().contains(searchText);

                case "Code":
                    return course.getCode() != null && course.getCode().toLowerCase().contains(searchText);

                case "All":
                default:
                    return matchesAllFields(course, searchText);
            }
        });
    }

    private boolean matchesAllFields(CourseDTO course, String searchText) {
        boolean matchesId = String.valueOf(course.getId()).contains(searchText);
        boolean matchesName = course.getName() != null && course.getName().toLowerCase().contains(searchText);
        boolean matchesCode = course.getCode() != null && course.getCode().toLowerCase().contains(searchText);
        /* TODO: Teacher ID add here later.
        boolean matchesTeacher = course.getUserId() != null && String.valueOf(course.getUserId()).contains(searchText);
        */
        return matchesId || matchesName || matchesCode;
    }

    // Dialogs
    private void showCourseInfoDialog(CourseDTO course) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Course Info");
        alert.setHeaderText(course.getName());
        alert.setContentText("Code: " + course.getCode());
        alert.showAndWait();
    }

    private void showAddCourseDialog() {
        Dialog<CourseDTO> dialog = new Dialog<>();
        dialog.setTitle("Add Course");

        TextField nameField = new TextField();
        nameField.setPromptText("Course name");

        TextField codeField = new TextField();
        codeField.setPromptText("Course code");

        VBox box = new VBox(10, nameField, codeField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                CourseDTO dto = new CourseDTO();
                dto.setCode(codeField.getText());
                dto.setName(nameField.getText());
                dto.setUserId(null);
                return dto;
            }
            return null;
        });

        CourseDTO result = dialog.showAndWait().orElse(null);

        if (result != null) {
            Course entity = courseMapper.toEntity(result);
            courseService.save(entity);
            loadCourses();
        }
    }

    private void showEditCourseDialog(CourseDTO course) {
        Dialog<CourseDTO> dialog = new Dialog<>();
        dialog.setTitle("Edit Course");

        TextField nameField = new TextField(course.getName());
        TextField codeField = new TextField(course.getCode());

        VBox box = new VBox(10, nameField, codeField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                course.setName(nameField.getText());
                course.setCode(codeField.getText());
                return course;
            }
            return null;
        });

        CourseDTO updated = dialog.showAndWait().orElse(null);

        if (updated != null) {
            Course entity = courseMapper.toEntity(updated);
            courseService.update(entity);
            loadCourses();
        }
    }

    private void showDeleteDialog(CourseDTO course) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Course");
        alert.setHeaderText("Are you sure you want to delete \"" + course.getName() + "\"?");
        alert.setContentText("This action cannot be undone.");

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yes, no);

        if (alert.showAndWait().orElse(no) == yes) {
            courseService.delete(course.getId());
            loadCourses();
        }
    }

    private void showAssessmentDialog(CourseDTO course) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Assessment");
        alert.setHeaderText("Teacher functionality");
        alert.setContentText("Here you will add assessments for: " + course.getName());
        alert.showAndWait();
    }
}
