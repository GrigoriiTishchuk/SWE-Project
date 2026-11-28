package com.edujournal.e2e;

import com.edujournal.Main;
import com.edujournal.config.LocalizationManager;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class TeacherE2ETest extends ApplicationTest {

    @Override
    public void start(Stage stage) {
        LocalizationManager.setLocale(Locale.US);

        new Main().start(stage);
        stage.show();
    }

    @Test
    void teacherCanViewCourses() {

        sleep(1000);

        clickOn(".text-field");
        write("teacher1");

        clickOn(".password-field");
        write("demo_password");

        clickOn("Sign in");

        sleep(1000);

        clickOn("View all courses");

        sleep(1000);

        assertTrue(
                lookup("Software Engineering").tryQuery().isPresent(),
                "Teacher should be able to view their assigned course"
        );
    }

    @Test
    void teacherCanChangeLanguage() {
        sleep(1000);

        TextField username = lookup(".text-field").queryAs(TextField.class);
        PasswordField password = lookup(".password-field").queryAs(PasswordField.class);

        interact(() -> {
            username.setText("teacher1");
            password.setText("demo_password");
        });

        clickOn("Sign in");
        sleep(1000);

        ComboBox<String> languageSelector = lookup(".combo-box")
                .queryAll()
                .stream()
                .map(node -> (ComboBox<String>) node)
                .filter(combo -> combo.getItems().contains("Türkçe"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Language selector not found"));

        interact(() -> languageSelector.setValue("Türkçe"));
        waitForFxEvents();
        sleep(1000);

        ComboBox<String> updatedSelector = lookup(".combo-box")
                .queryAll()
                .stream()
                .map(node -> (ComboBox<String>) node)
                .filter(combo -> combo.getItems().contains("Türkçe"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Language selector not found after reload"));

        assertEquals("Türkçe", updatedSelector.getValue());

        interact(() -> updatedSelector.setValue("English"));
        waitForFxEvents();
        sleep(1000);

        assertEquals(
                "English",
                lookup(".combo-box")
                        .queryAll()
                        .stream()
                        .map(node -> (ComboBox<String>) node)
                        .filter(combo -> combo.getItems().contains("Türkçe"))
                        .findFirst()
                        .orElseThrow()
                        .getValue()
        );
    }

}
