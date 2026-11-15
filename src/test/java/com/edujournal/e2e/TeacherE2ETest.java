package com.edujournal.e2e;

import com.edujournal.Main;
import com.edujournal.config.LocalizationManager;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

}
