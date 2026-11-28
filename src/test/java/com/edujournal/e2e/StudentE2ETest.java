package com.edujournal.e2e;

import com.edujournal.Main;
import com.edujournal.config.LocalizationManager;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class StudentE2ETest extends ApplicationTest {

    @Override
    public void start(Stage stage) {
        LocalizationManager.setLocale(Locale.US);

        new Main().start(stage);
        stage.show();
    }

    @Test
    void studentCanViewProfile() {

        sleep(1000);

        clickOn(".text-field");
        write("student1");

        clickOn(".password-field");
        write("demo_password");

        clickOn("Sign in");

        sleep(1000);

        clickOn("Own Profile");

        sleep(1000);

        assertTrue(
                lookup("Personal Information").tryQuery().isPresent(),
                "Student should be able to view their own profile"
        );

        assertTrue(
                lookup("Student Number:").tryQuery().isPresent(),
                "Student profile should show the student number"
        );

        assertTrue(
                lookup("Date of Birth").tryQuery().isPresent(),
                "Student profile should show the date of birth"
        );

        assertTrue(
                lookup("Group:").tryQuery().isPresent(),
                "Student profile should show the academic group"
        );
    }

    @Test
    void studentCanViewReport() {

        sleep(1000);

        clickOn(".text-field");
        write("student1");

        clickOn(".password-field");
        write("demo_password");

        clickOn("Sign in");

        sleep(1000);

        clickOn("Student's Report");

        sleep(1000);

        assertTrue(
                lookup("Student's Report").tryQuery().isPresent(),
                "Student should be able to view their report"
        );
    }

    @Test
    void userCanLogout() {
        sleep(1000);

        clickOn(".text-field");
        write("student1");

        clickOn(".password-field");
        write("demo_password");

        clickOn("Sign in");

        sleep(1000);

        clickOn("⏻");

        sleep(1000);

        assertTrue(
                lookup("Sign in").tryQuery().isPresent(),
                "User should be returned to the login page after logout"
        );
    }

    @Test
    void studentCanChangeLanguage() {
        sleep(1000);

        TextField username = lookup(".text-field").queryAs(TextField.class);
        PasswordField password = lookup(".password-field").queryAs(PasswordField.class);

        interact(() -> {
            username.setText("student1");
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
