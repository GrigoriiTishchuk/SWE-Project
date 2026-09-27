package com.edujournal.e2e;

import com.edujournal.Main;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TeacherE2ETest extends ApplicationTest {

    @Override
    public void start(Stage stage) {
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
