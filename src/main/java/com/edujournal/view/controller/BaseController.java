package com.edujournal.view.controller;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BaseController<T> extends BorderPane {

    protected TableView<T> table = new TableView<>();
    protected TextField searchField = new TextField();
    protected ComboBox<String> filterCombo = new ComboBox<>();

    protected Button addBtn = new Button("Add");
    protected Button editBtn = new Button("Edit");
    protected Button deleteBtn = new Button("Delete");
    protected Button viewBtn = new Button("View");

    protected boolean useCards = false;
    protected Function<T, Node> cardBuilder = null;

    public BaseController() {
        setupSearchHandlers();
        setupLayout();
        setupButtons();
        setupDoubleClick();
    }

    // Abstract methods for children
    protected abstract void configureColumns();
    protected abstract List<T> loadAllItems();
    protected abstract boolean matchesFilter(T item, String filter, String text);

    protected abstract void showAddDialog();
    protected abstract void showEditDialog();
    protected abstract void showDeleteDialog();
    protected abstract void onView(); // optional

    // Layout
    private void setupLayout() {

        searchField.setPromptText("Search...");
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

        VBox top = new VBox(filterBar);
        top.setPadding(new Insets(10));

        setTop(top);
        setCenter(table);
    }

    // Buttons
    private void setupButtons() {

        addBtn.setOnAction(e -> showAddDialog());
        editBtn.setOnAction(e -> showEditDialog());
        deleteBtn.setOnAction(e -> showDeleteDialog());
        viewBtn.setOnAction(e -> onView());

        HBox bottom = new HBox(10, addBtn, editBtn, deleteBtn, viewBtn);
        bottom.setPadding(new Insets(10));

        setBottom(bottom);
    }

    // Double click
    private void setupDoubleClick() {
        table.setRowFactory(tv -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    onView();
                }
            });
            return row;
        });
    }

    // Search + Filter
    private void setupSearchHandlers() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());
        filterCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilter());
    }

    protected void loadAndShowItems() {
        List<T> items = loadAllItems();

        if (cardBuilder != null) {
            VBox list = new VBox(10);
            for (T item : items) {
                list.getChildren().add(cardBuilder.apply(item));
            }
            ScrollPane scroll = new ScrollPane(list);
            scroll.setFitToWidth(true);
            scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            setCenter(scroll);
        } else {
            table.setItems(FXCollections.observableArrayList(items));
            ScrollPane scroll = new ScrollPane(table);
            scroll.setFitToWidth(true);
            scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            setCenter(scroll);
        }
    }

    protected void applyFilter() {
        String filter = filterCombo.getValue();
        String text = searchField.getText().toLowerCase();

        List<T> all = loadAllItems();

        List<T> filtered = all.stream()
                .filter(item -> matchesFilter(item, filter, text))
                .collect(Collectors.toList());

        if (cardBuilder != null) {
            table.getItems().clear();
            VBox list = new VBox(10);
            for (T item : filtered) {
                list.getChildren().add(cardBuilder.apply(item));
            }

            ScrollPane scroll = new ScrollPane(list);
            scroll.setFitToWidth(true);
            scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            setCenter(scroll);
        } else {
            table.setItems(FXCollections.observableArrayList(filtered));
            ScrollPane scroll = new ScrollPane(table);
            scroll.setFitToWidth(true);
            scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            setCenter(scroll);
        }
    }

    protected boolean matchesAllFields(List<String> fields, String text) {
        for (String f : fields) {
            if (f != null && f.toLowerCase().contains(text)) {
                return true;
            }
        }
        return false;
    }

    protected <T> ComboBox<T> createComboBox(
            List<T> items,
            T selectedItem,
            String promptText,
            String nullText,
            Function<T, String> displayFunction
    ) {
        List<T> options = new ArrayList<>();
        options.add(null);
        options.addAll(items);

        ComboBox<T> combo = new ComboBox<>();
        combo.setItems(FXCollections.observableArrayList(options));
        combo.setPromptText(promptText);

        combo.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else if (item == null) {
                    setText(nullText);
                } else {
                    setText(displayFunction.apply(item));
                }
            }
        });

        combo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else if (item == null) {
                    setText(nullText);
                } else {
                    setText(displayFunction.apply(item));
                }
            }
        });

        combo.setValue(selectedItem);

        return combo;
    }

    protected void enableCardsView(Function<T, Node> builder) {
        this.cardBuilder = builder;
    }

}
