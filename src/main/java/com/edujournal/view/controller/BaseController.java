package com.edujournal.view.controller;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseController<T> extends BorderPane {

    protected TableView<T> table = new TableView<>();
    protected TextField searchField = new TextField();
    protected ComboBox<String> filterCombo = new ComboBox<>();

    public BaseController() {
        setupSearchHandlers();
    }

    protected abstract void configureColumns();

    protected abstract List<T> loadAllItems();

    protected abstract boolean matchesFilter(T item, String filter, String text);

    private void setupSearchHandlers() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());
        filterCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilter());
    }

    protected void loadAndShowItems() {
        table.setItems(FXCollections.observableArrayList(loadAllItems()));
        applyFilter();
    }

    protected void applyFilter() {
        String filter = filterCombo.getValue();
        String text = searchField.getText().toLowerCase();

        List<T> all = loadAllItems();

        List<T> filtered = all.stream()
                .filter(item -> matchesFilter(item, filter, text))
                .collect(Collectors.toList());

        table.setItems(FXCollections.observableArrayList(filtered));
    }

    protected boolean matchesAllFields(List<String> fields, String text) {
        for (String f : fields) {
            if (f != null && f.toLowerCase().contains(text)) {
                return true;
            }
        }
        return false;
    }
}
