package com.edujournal.view.controller;

import com.edujournal.backend.service.AcademicGroupService;
import com.edujournal.backend.service.CourseService;
import com.edujournal.model.AcademicGroupDTO;
import com.edujournal.model.CourseDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class AdminGroupController extends BaseController<AcademicGroupDTO> {

    private final AcademicGroupService groupService = new AcademicGroupService();
    private final CourseService courseService = new CourseService();

    public AdminGroupController() {
        configureColumns();
        setupLayout();
        loadAndShowItems();
    }

    private void setupLayout() {
        VBox top = new VBox(10, searchField, filterCombo);
        top.setPadding(new Insets(10));

        setTop(top);
        setCenter(table);
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
}
