package com.edujournal.view.controller;

import com.edujournal.backend.service.AcademicGroupService;
import com.edujournal.backend.service.CourseService;
import com.edujournal.backend.utils.AcademicGroupMapper;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.entity.Course;
import com.edujournal.model.AcademicGroupDTO;
import com.edujournal.model.CourseDTO;
import com.edujournal.model.UserDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class AdminGroupController extends BaseController<AcademicGroupDTO> {

    private final AcademicGroupService groupService = new AcademicGroupService();
    private final AcademicGroupMapper groupMapper = new AcademicGroupMapper();
    private final CourseService courseService = new CourseService();

    private AcademicGroupDTO selectedGroup;

    public AdminGroupController() {
        enableCardsView(this::buildGroupCard);
        viewBtn.setVisible(false);
        configureColumns();
        loadAndShowItems();
    }

    @Override
    protected void configureColumns() {
        TableColumn<AcademicGroupDTO, String> nameCol =
                new TableColumn<>("Group Name");
        nameCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getName()));

        table.getColumns().add(nameCol);

        filterCombo.getItems().addAll("All", "Name");
        filterCombo.setValue("All");
    }

    private VBox buildGroupCard(AcademicGroupDTO group) {
        VBox card = new VBox();
        card.setStyle("-fx-border-color: #ccc; -fx-padding: 10; -fx-background-color: #f9f9f9;");
        card.setSpacing(5);

        Label nameLabel = new Label(group.getName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label arrow = new Label("▶");
        arrow.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        HBox header = new HBox(nameLabel, arrow);
        header.setSpacing(10);
        header.setStyle("-fx-alignment: center-left;");

        VBox coursesBox = new VBox();
        coursesBox.setSpacing(5);
        coursesBox.setPadding(new Insets(0, 0, 0, 20));
        coursesBox.setVisible(false);

        List<CourseDTO> courses = courseService.findByGroupId(group.getId());

        for (CourseDTO c : courses) {
            Label courseLabel = new Label("• " + c.getName() + " (" + c.getCode() + ")");
            coursesBox.getChildren().add(courseLabel);
        }

        card.setOnMouseClicked(e -> {
            selectedGroup = group;

            VBox parent = (VBox) card.getParent();
            if (parent != null) {
                for (Node node : parent.getChildren()) {
                    node.setStyle("-fx-border-color: #ccc; -fx-padding: 10; -fx-background-color: #f9f9f9;");
                }
            }
            card.setStyle("-fx-border-color: #4A90E2; -fx-padding: 10; -fx-background-color: #E8F0FE;");
        });

        header.setOnMouseClicked(e -> {
            boolean expanded = coursesBox.isVisible();
            coursesBox.setVisible(!expanded);
            arrow.setText(expanded ? "▶" : "▼");
        });

        card.getChildren().addAll(header, coursesBox);
        return card;
    }

    @Override
    protected List<AcademicGroupDTO> loadAllItems() {
        return groupService.findAllDTO();
    }

    @Override
    protected boolean matchesFilter(AcademicGroupDTO item, String filter, String text) {

        if (filter == null || filter.equals("All")) {
            return matchesAllFields(List.of(item.getName()), text);
        }

        if (filter.equals("Name")) {
            return item.getName() != null &&
                    item.getName().toLowerCase().contains(text);
        }
        return true;
    }

    @Override
    protected void showAddDialog() {
        Dialog<AcademicGroupDTO> dialog = new Dialog<>();
        dialog.setTitle("Add Academic Group");

        TextField nameField = new TextField();
        nameField.setPromptText("Academic group name");

        VBox box = new VBox(10, nameField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == saveBtn) {
                if (groupService.existsByName(nameField.getText())) {
                    new Alert(Alert.AlertType.ERROR,
                            "Academic group already exists.").showAndWait();
                    return null;
                }

                if (nameField.getText().isBlank()) {
                    new Alert(Alert.AlertType.ERROR,
                            "Group name cannot be empty.").showAndWait();
                    return null;
                }

                AcademicGroupDTO dto = new AcademicGroupDTO();
                dto.setName(nameField.getText());
                return dto;
            }
            return null;
        });

        AcademicGroupDTO result = dialog.showAndWait().orElse(null);

        if (result != null) {
            AcademicGroup entity = groupMapper.toEntity(result);
            groupService.save(entity);
            loadAndShowItems();
        }
    }

    @Override
    protected void showEditDialog() {
        AcademicGroupDTO group = selectedGroup;
        if (group == null) {
            return;
        }

        Dialog<AcademicGroupDTO> dialog = new Dialog<>();
        dialog.setTitle("Edit Academic Group");

        TextField nameField = new TextField(group.getName());

        VBox box = new VBox(10, nameField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                String newName = nameField.getText().trim();

                if (newName.equalsIgnoreCase(group.getName())) {
                } else {
                    if (groupService.existsByName(newName)) {
                        new Alert(Alert.AlertType.ERROR,
                                "Academic group already exists.").showAndWait();
                        return null;
                    }
                }

                if (nameField.getText().isBlank()) {
                    new Alert(Alert.AlertType.ERROR,
                            "Group name cannot be empty.").showAndWait();
                    return null;
                }

                group.setName(nameField.getText());
                return group;
            }
            return null;
        });

        AcademicGroupDTO updated = dialog.showAndWait().orElse(null);

        if (updated != null) {
            AcademicGroup entity = groupMapper.toEntity(updated);
            groupService.update(entity);
            loadAndShowItems();
            clearSelection();
        }
    }

    @Override
    protected void showDeleteDialog() {
        AcademicGroupDTO group = selectedGroup;
        if (group == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Group");
        alert.setHeaderText("Are you sure you want to delete \"" + group.getName() + "\"?");
        alert.setContentText("This action cannot be undone.");

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yes, no);

        if (alert.showAndWait().orElse(no) == yes) {
            groupService.delete(group.getId());

            loadAndShowItems();
            clearSelection();
        }
    }

    @Override
    protected void onView() {}

    private void clearSelection() {
        selectedGroup = null;
    }
}
