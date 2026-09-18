package com.edujournal.view.controller;

import com.edujournal.backend.service.CourseService;
import com.edujournal.backend.service.UserService;
import com.edujournal.backend.utils.CourseMapper;
import com.edujournal.entity.Course;
import com.edujournal.entity.Role;
import com.edujournal.model.CourseDTO;
import com.edujournal.model.UserDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class CourseController extends BaseController<CourseDTO> {

    private final CourseService courseService = new CourseService();
    private final CourseMapper courseMapper = new CourseMapper();
    private final UserService userService = new UserService();

    private final Role role;

    public CourseController(Role role) {
        this.role = role;
        configureColumns();
        buildUI();
        loadAndShowItems();
    }

    private HBox buildFilterBar() {
        searchField.setPromptText("Search courses...");
        searchField.setPrefWidth(250);

        Button clearButton = new Button("Clear");
        HBox filterBar = new HBox(10);
        filterBar.setPadding(new Insets(10, 0, 10, 0));
        filterBar.getChildren().addAll(
                new Label("Search:"),
                searchField,
                new Label("Filter by:"),
                filterCombo,
                clearButton
        );

        clearButton.setOnAction(event -> {
            searchField.clear();
            filterCombo.setValue("All");
        });

        return filterBar;
    }

    private void buildUI() {
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

    @Override
    protected List<CourseDTO> loadAllItems() {
        return courseService.findAll();
    }

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

            case "All":
            default:
                return matchesAllFields(
                        List.of(
                                String.valueOf(course.getId()),
                                safe(course.getName()),
                                safe(course.getCode()),
                                safe(course.getTeacherName())
                        ),
                        searchText
                );
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    @Override
    protected void configureColumns() {
        TableColumn<CourseDTO, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getId())));

        TableColumn<CourseDTO, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCode()));

        TableColumn<CourseDTO, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));

        TableColumn<CourseDTO, String> teacherCol = new TableColumn<>("Teacher");
        teacherCol.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getTeacherName() == null
                                ? ""
                                : c.getValue().getTeacherName()
                )
        );

        table.getColumns().addAll(idCol, codeCol, nameCol, teacherCol);

        filterCombo.getItems().addAll("All", "ID", "Name", "Code", "Teacher");
        filterCombo.setValue("All");
    }

    // Dialogs
    private void showCourseInfoDialog(CourseDTO course) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Course Info");
        alert.setHeaderText(course.getName());
        alert.setContentText("Code: " + course.getCode() + "\nTeacher: " + course.getTeacherName());
        alert.showAndWait();
    }

    private void showAddCourseDialog() {
        Dialog<CourseDTO> dialog = new Dialog<>();
        dialog.setTitle("Add Course");

        TextField nameField = new TextField();
        nameField.setPromptText("Course name");

        TextField codeField = new TextField();
        codeField.setPromptText("Course code");

        List<UserDTO> teachers = userService.findAllTeachers();

        List<Object> teacherOptions = new ArrayList<>();
        teacherOptions.add(null);
        teacherOptions.addAll(teachers);

        ComboBox<Object> teacherCombo = new ComboBox<>();
        teacherCombo.setItems(FXCollections.observableArrayList(teacherOptions));
        teacherCombo.setPromptText("Select teacher");

        teacherCombo.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText(null);
                } else if (item == null) {
                    setText("— No teacher —");
                } else {
                    UserDTO u = (UserDTO) item;
                    setText(u.getFirstName() + " " + u.getLastName());
                }
            }
        });

        teacherCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText(null);
                } else if (item == null) {
                    setText("— No teacher —");
                } else {
                    UserDTO u = (UserDTO) item;
                    setText(u.getFirstName() + " " + u.getLastName());
                }
            }
        });

        VBox box = new VBox(10, nameField, codeField, teacherCombo);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == saveBtn) {
                String code = codeField.getText();

                if (courseService.existsByCode(code)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Course code already exists. Please try again.");
                    alert.showAndWait();
                    return null;
                }

                CourseDTO dto = new CourseDTO();
                dto.setCode(code);
                dto.setName(nameField.getText());

                Object selected = teacherCombo.getValue();
                if (selected == null) {
                    dto.setUserId(null);
                } else {
                    UserDTO u = (UserDTO) selected;
                    dto.setUserId(u.getId());
                }
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

    private void showEditCourseDialog(CourseDTO course) {
        Dialog<CourseDTO> dialog = new Dialog<>();
        dialog.setTitle("Edit Course");

        TextField nameField = new TextField(course.getName());
        TextField codeField = new TextField(course.getCode());

        List<UserDTO> teachers = userService.findAllTeachers();

        List<UserDTO> teacherOptions = new ArrayList<>();
        teacherOptions.add(null);
        teacherOptions.addAll(teachers);

        ComboBox<UserDTO> teacherCombo = new ComboBox<>();
        teacherCombo.setItems(FXCollections.observableArrayList(teacherOptions));
        teacherCombo.setPromptText("Select teacher");

        teacherCombo.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(UserDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else if (item == null) {
                    setText("— Remove teacher —");
                } else {
                    UserDTO u = (UserDTO) item;
                    setText(u.getFirstName() + " " + u.getLastName());
                }
            }
        });

        teacherCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(UserDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else if (item == null) {
                    setText("— Remove teacher —");
                } else {
                    UserDTO u = (UserDTO) item;
                    setText(u.getFirstName() + " " + u.getLastName());
                }
            }
        });

        if (course.getUserId() != null) {
            teachers.stream()
                    .filter(t -> t.getId().equals(course.getUserId()))
                    .findFirst()
                    .ifPresent(teacherCombo::setValue);
        }

        VBox box = new VBox(10, nameField, codeField, teacherCombo);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                course.setName(nameField.getText());
                course.setCode(codeField.getText());

                Object selected = teacherCombo.getValue();

                if (selected == null) {
                    course.setUserId(null);
                } else {
                    UserDTO u = (UserDTO) selected;
                    course.setUserId(u.getId());
                }
                return course;
            }
            return null;
        });

        CourseDTO updated = dialog.showAndWait().orElse(null);

        if (updated != null) {
            Course entity = courseMapper.toEntity(updated);
            courseService.update(entity);
            courseService.update(entity);
            loadAndShowItems();
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
            loadAndShowItems();
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
