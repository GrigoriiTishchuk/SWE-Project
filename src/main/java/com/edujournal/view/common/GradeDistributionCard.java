package com.edujournal.view.common;

import com.edujournal.backend.service.DashboardStatisticsService;
import com.edujournal.entity.Role;
import com.edujournal.model.GradeDistributionDTO;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;

public class GradeDistributionCard extends VBox {
    private final Role role;
    private final BarChart<String, Number> chart;

    public GradeDistributionCard(Role role) {
        this.role = role;
        DashboardStatisticsService statisticsService = new DashboardStatisticsService();

        setSpacing(10);
        setPadding(new Insets(15));

        setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #DDDDDD;");

        Label title = new Label("");
        if (role == Role.ADMINISTRATOR) {
            title.setText("Average Grade");
        } else if (role == Role.TEACHER) {
            title.setText("My Courses Average Grade");
        } else {
            title.setText("Personal Grade Distribution");
        }

        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        chart = new BarChart<>(xAxis, yAxis);

        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setPrefHeight(220);
        setPrefWidth(300);

        GradeDistributionDTO dto =
                statisticsService.calculateDistribution(
                        statisticsService.getAdministratorGrades()
                );

        fillChart(
                dto.getExcellent(),
                dto.getVeryGood(),
                dto.getGood(),
                dto.getSatisfactory(),
                dto.getSufficient(),
                dto.getFail()
        );

        VBox legend = new VBox(5);

        legend.getChildren().addAll(
                createLegendItem(
                        "Excellent",
                        "#5B4EE4",
                        dto.getExcellent()
                ),
                createLegendItem(
                        "Very Good",
                        "#1E88E5",
                        dto.getVeryGood()
                ),
                createLegendItem(
                        "Good",
                        "#4CAF50",
                        dto.getGood()
                ),
                createLegendItem(
                        "Satisfactory",
                        "#E6C200",
                        dto.getSatisfactory()
                ),
                createLegendItem(
                        "Sufficient",
                        "#FF8C00",
                        dto.getSufficient()
                ),
                createLegendItem(
                        "Fail",
                        "#FF3B3B",
                        dto.getFail()
                )
        );

        getChildren().addAll(title, chart, legend);
    }

    private HBox createLegendItem(String text, String color, double value) {
        Region colorBox = new Region();
        colorBox.setPrefSize(14, 14);
        colorBox.setStyle("-fx-background-color:" + color + ";");

        Label nameLabel = new Label(text);
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Label valueLabel = new Label(String.format("%.1f %%", value));
        Region spacer = new Region();

        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox row = new HBox(10, colorBox, nameLabel, spacer, valueLabel);

        return row;
    }

    private void fillChart(double excellent, double veryGood, double good, double satisfactory, double sufficient, double fail) {

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("E", excellent));
        series.getData().add(new XYChart.Data<>("VG", veryGood));
        series.getData().add(new XYChart.Data<>("G", good));
        series.getData().add(new XYChart.Data<>("S", satisfactory));
        series.getData().add(new XYChart.Data<>("SF", sufficient));
        series.getData().add(new XYChart.Data<>("F", fail));
        chart.getData().add(series);

        String[] colors = {
                "#5B4EE4",
                "#1E88E5",
                "#4CAF50",
                "#E6C200",
                "#FF8C00",
                "#FF3B3B"
        };

        Platform.runLater(() -> {
            for (int i = 0; i < series.getData().size(); i++) {

                XYChart.Data<String, Number> data =
                        series.getData().get(i);

                if (data.getNode() != null) {

                    data.getNode().setStyle(
                            "-fx-bar-fill: " + colors[i] + ";"
                    );
                }
            }
        });
    }
}