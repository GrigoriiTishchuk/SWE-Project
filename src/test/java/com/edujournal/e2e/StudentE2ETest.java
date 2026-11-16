package com.edujournal.e2e;

import com.edujournal.Main;
import com.edujournal.config.LocalizationManager;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertTrue;

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


}
