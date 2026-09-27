package com.edujournal.e2e;

import com.edujournal.Main;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthenticationE2ETest extends ApplicationTest {

    @Override
    public void start(Stage stage) {
        new Main().start(stage);
        stage.show();
    }

    @Test
    void validLoginShouldOpenDashboard() {

        sleep(1000);

        clickOn(".text-field");
        write("admin");

        clickOn(".password-field");
        write("demo_password");

        clickOn("Sign in");

        sleep(1000);

        assertTrue(
                lookup("Quick Actions").tryQuery().isPresent(),
                "Successful login should open the admin dashboard"
        );
    }

    @Test
    void invalidLoginShouldShowError() {

        sleep(1000);

        clickOn(".text-field");
        write("wrong_user");

        clickOn(".password-field");
        write("wrong_password");

        clickOn("Sign in");

        sleep(500);

        assertTrue(
                lookup("Invalid username or password.").tryQuery().isPresent(),
                "Invalid login should show an error message"
        );
    }
}