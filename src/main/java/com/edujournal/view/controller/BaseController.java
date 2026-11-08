package com.edujournal.view.controller;

import com.edujournal.config.LocalizationManager;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Base controller for entity management screens.
 * Provides common functionality for displaying, filtering and managing data in table or card views.
 *
 * @param <T> entity type displayed by the controller
 */
public abstract class BaseController<T> extends BorderPane {

    protected final TableView<T> table = new TableView<>();
    protected final TextField searchField = new TextField();
    protected final ComboBox<String> filterCombo = new ComboBox<>();

    protected final Button addBtn    = new Button(LocalizationManager.get("add"));
    protected final Button editBtn   = new Button(LocalizationManager.get("edit"));
    protected final Button deleteBtn = new Button(LocalizationManager.get("delete"));
    protected final Button viewBtn   = new Button(LocalizationManager.get("view"));
    protected final Button reportBtn = new Button(LocalizationManager.get("studentReport"));

    {
        addBtn.getStyleClass().add("btn-primary");
        editBtn.getStyleClass().add("btn-secondary");
        deleteBtn.getStyleClass().add("btn-delete");
        viewBtn.getStyleClass().add("btn-secondary");
        reportBtn.getStyleClass().add("btn-secondary");
    }

    protected Function<T, Node> cardBuilder = null;

    public BaseController() {
        getStylesheets().add(BaseController.class.getResource("/css/table.css").toExternalForm());
        getStylesheets().add(BaseController.class.getResource("/css/button.css").toExternalForm());
        setupSearchHandlers();
        setupLayout();
        setupButtons();
        setupDoubleClick();
    }

    /**
     * Configures table columns for the entity view.
     */
    protected abstract void configureColumns();

    /**
     * Loads all available entities.
     *
     * @return list of entities
     */
    protected abstract List<T> loadAllItems();

    /**
     * Checks whether an entity matches
     * current search and filter criteria.
     *
     * @param item entity to evaluate
     * @param filter selected filter value
     * @param text search text
     * @return true if entity should be displayed
     */
    protected abstract boolean matchesFilter(T item, String filter, String text);

    /**
     * Opens a dialog for creating a new entity.
     */
    protected abstract void showAddDialog();

    /**
     * Opens a dialog for editing the selected entity.
     */
    protected abstract void showEditDialog();

    /**
     * Opens a dialog for deleting the selected entity.
     */
    protected abstract void showDeleteDialog();

    /**
     * Displays detailed information about the selected entity.
     */
    protected abstract void onView(); // optional

    /**
     * Layout setup
     */
    private void setupLayout() {

        table.setPlaceholder(new Label(LocalizationManager.get("noContent")));
        searchField.setPromptText(LocalizationManager.get("search"));
        searchField.setPrefWidth(250);

        Button clearButton = new Button(LocalizationManager.get("clear"));

        HBox filterBar = new HBox(10);
        filterBar.setPadding(new Insets(10, 0, 10, 0));
        filterBar.getChildren().addAll(
                searchField,
                new Label(LocalizationManager.get("filterBy")),
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

    /**
     * Buttons setup
     */
    private void setupButtons() {

        addBtn.setOnAction(e -> showAddDialog());
        editBtn.setOnAction(e -> showEditDialog());
        deleteBtn.setOnAction(e -> showDeleteDialog());
        viewBtn.setOnAction(e -> onView());
        reportBtn.setDisable(true);
        reportBtn.setVisible(false);
        reportBtn.setManaged(false);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox bottom = new HBox(10, addBtn, editBtn, deleteBtn, viewBtn, spacer, reportBtn);
        bottom.setPadding(new Insets(10));

        setBottom(bottom);
    }

    /**
     * Double click
     */
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

    /**
     * Search + Filter
     */
    private void setupSearchHandlers() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());
        filterCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilter());
    }

    /**
     * Creates a configured scroll pane for displaying content.
     *
     * @param content content to display
     * @return configured scroll pane
     */
    private ScrollPane createScrollPane(Node content) {
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        return scroll;
    }

    /**
     * Builds a vertical card list from entities.
     *
     * @param items entities to display
     * @return VBox containing generated cards
     */
    private VBox createCardList(List<T> items) {
        VBox list = new VBox(10);
        for (T item : items) {
            list.getChildren().add(cardBuilder.apply(item));
        }
        return list;
    }

    /**
     * Loads data from the source and displays it either in a table or card layout.
     */
    protected void loadAndShowItems() {
        List<T> items = loadAllItems();

        if (cardBuilder != null) {
            VBox list = createCardList(items);
            setCenter(createScrollPane(list));
        } else {
            table.setItems(FXCollections.observableArrayList(items));
            setCenter(createScrollPane(table));
        }
    }

    /**
     * Applies search text and selected filter to currently loaded items.
     */
    protected void applyFilter() {
        String filter = filterCombo.getValue();
        String text = searchField.getText().toLowerCase();

        List<T> all = loadAllItems();

        List<T> filtered = all.stream()
                .filter(item -> matchesFilter(item, filter, text))
                .collect(Collectors.toList());

        if (cardBuilder != null) {
            table.getItems().clear();
            VBox list = createCardList(filtered);

            setCenter(createScrollPane(list));
        } else {
            table.setItems(FXCollections.observableArrayList(filtered));
            setCenter(createScrollPane(table));
        }
    }

    /**
     * Checks whether any field contains the specified search text.
     *
     * @param fields values to search in
     * @param text search text
     * @return true if at least one field matches
     */
    protected boolean matchesAllFields(List<String> fields, String text) {
        for (String f : fields) {
            if (f != null && f.toLowerCase().contains(text)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a ComboBox with custom display values
     * and optional empty selection.
     *
     * @param items available options
     * @param selectedItem initially selected item
     * @param promptText placeholder text
     * @param nullText text displayed for null value
     * @param displayFunction item display formatter
     * @return configured ComboBox
     */
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

    /**
     * Enables card-based presentation of entities.
     *
     * @param builder function that converts an entity into a JavaFX node
     */
    protected void enableCardsView(Function<T, Node> builder) {
        this.cardBuilder = builder;
    }

    /**
     * Displays an error dialog with the specified message.
     *
     * @param message error message
     */
    protected void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message)
                .showAndWait();
    }

    /**
     * Creates a localized list cell for displaying translated filter values in ComboBox controls.
     *
     * @param keyMap mapping between display values
     *               and localization keys
     * @return localized list cell
     */
    protected ListCell<String> createLocalizedCell(Map<String, String> keyMap) {
        return new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    return;
                }

                String key = keyMap.get(item);
                setText(key != null ? LocalizationManager.get(key) : item);
            }
        };
    }
}

