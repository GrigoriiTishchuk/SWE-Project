package com.edujournal.e2e;

import com.edujournal.Main;
import com.edujournal.config.LocalizationManager;
import com.edujournal.entity.Role;
import com.edujournal.entity.User;
import com.edujournal.backend.service.AcademicGroupService;
import com.edujournal.backend.service.CourseService;
import com.edujournal.backend.service.StudentService;
import com.edujournal.backend.service.UserService;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.scene.control.DatePicker;
import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

public class AdminE2ETest extends ApplicationTest {

    @Override
    public void start(Stage stage) {
        LocalizationManager.setLocale(Locale.US);

        new Main().start(stage);
        stage.show();
    }

    @AfterEach
    void cleanupTestData() {
        CourseService courseService = new CourseService();
        AcademicGroupService groupService = new AcademicGroupService();
        StudentService studentService = new StudentService();
        UserService userService = new UserService();

        String[] courseCodes = {
                "E2E101",
                "E2EFULL101"
        };

        String[] groupNames = {
                "E2E Group",
                "E2E Full Group",
                "E2ECourse Group"
        };

        String[] studentNames = {
                "E2EStudent|TestUser",
                "E2EFullStudent|TestUser"
        };

        String[] teacherNames = {
                "E2ETeacher|TestUser",
                "E2EFullTeacher|TestUser"
        };

        for (String courseCode : courseCodes) {
            try {
                var course = courseService.findByCode(courseCode);

                if (course != null) {
                    courseService.delete(course.getId());
                }
            } catch (Exception ignored) {
            }
        }

        for (String studentName : studentNames) {
            try {
                String[] parts = studentName.split("\\|");

                User studentUser = userService.findAll()
                        .stream()
                        .filter(user ->
                                user.getRole() == Role.STUDENT
                                        && parts[0].equals(user.getFirstName())
                                        && parts[1].equals(user.getLastName())
                        )
                        .findFirst()
                        .orElse(null);

                if (studentUser != null) {
                    var student = studentService.findByUserId(studentUser.getId());

                    if (student != null) {
                        for (String groupName : groupNames) {
                            try {
                                var group = groupService.findByName(groupName);

                                if (group != null) {
                                    groupService.removeStudentFromGroup(
                                            student.getId(),
                                            group.getId()
                                    );
                                }
                            } catch (Exception ignored) {
                            }
                        }

                        try {
                            studentService.delete(student.getId());
                        } catch (Exception ignored) {
                        }
                    }

                    try {
                        userService.deleteUser(studentUser.getId());
                    } catch (Exception ignored) {
                    }
                }
            } catch (Exception ignored) {
            }
        }

        for (String groupName : groupNames) {
            try {
                var group = groupService.findByName(groupName);

                if (group != null) {
                    groupService.delete(group.getId());
                }
            } catch (Exception ignored) {
            }
        }

        for (String teacherName : teacherNames) {
            try {
                String[] parts = teacherName.split("\\|");

                User teacherUser = userService.findAll()
                        .stream()
                        .filter(user ->
                                user.getRole() == Role.TEACHER
                                        && parts[0].equals(user.getFirstName())
                                        && parts[1].equals(user.getLastName())
                        )
                        .findFirst()
                        .orElse(null);

                if (teacherUser != null) {
                    userService.deleteUser(teacherUser.getId());
                }
            } catch (Exception ignored) {
            }
        }
    }

    @Test
    void adminCanCreateStudent() {
        sleep(1200);

        TextField username = lookup(".text-field").queryAs(TextField.class);
        PasswordField password = lookup(".password-field").queryAs(PasswordField.class);

        interact(() -> {
            username.setText("admin");
            password.setText("demo_password");
        });

        clickOn("Sign in");
        sleep(1200);

        clickOn("Add student");
        sleep(700);

        clickOn("First Name");
        write("E2EStudent");

        clickOn("Last Name");
        write("TestUser");

        push(KeyCode.TAB);
        write("5551234567");

        clickOn("Save");
        sleep(1200);

        assertTrue(
                lookup("E2EStudent").tryQuery().isPresent(),
                "Created student should appear in the student list"
        );
    }

    @Test
    void adminCanCreateTeacher() {
        sleep(1200);

        clickOn(".text-field");
        write("admin");

        clickOn(".password-field");
        write("demo_password");

        clickOn("Sign in");
        sleep(1200);

        clickOn("Add teacher");
        sleep(700);

        clickOn("First Name");
        write("E2ETeacher");

        clickOn("Last Name");
        write("TestUser");

        push(KeyCode.TAB);
        write("5551234567");

        clickOn("Save");
        sleep(1200);

        assertTrue(
                lookup("E2ETeacher").tryQuery().isPresent(),
                "Created teacher should appear in the teacher list"
        );
    }

    @Test
    void adminCanCreateAcademicGroup() {
        sleep(1200);

        clickOn(".text-field");
        write("admin");

        clickOn(".password-field");
        write("demo_password");

        clickOn("Sign in");
        sleep(1200);

        clickOn("Add group");
        sleep(700);

        clickOn("#groupNameField");
        write("E2E Group");

        clickOn("Save");
        sleep(2000);

        assertTrue(
                lookup("E2E Group").tryQuery().isPresent(),
                "Created academic group should appear in the group list"
        );
    }

    @Test
    void adminCanCreateCourse() {
        sleep(1200);

        clickOn(".text-field");
        write("admin");

        clickOn(".password-field");
        write("demo_password");

        clickOn("Sign in");
        sleep(1200);

        clickOn("Add group");
        sleep(700);

        clickOn("#groupNameField");
        write("E2ECourse Group");

        clickOn("Save");
        sleep(2000);

        assertTrue(
                lookup("E2ECourse Group").tryQuery().isPresent(),
                "Created academic group should appear in the group list"
        );

        clickOn("Dashboard");
        sleep(1200);

        clickOn("Add course");
        sleep(1000);

        clickOn("#courseNameField");
        write("E2E Course");

        clickOn("#courseCodeField");
        write("E2E101");

        Node teacherComboBox = lookup(".combo-box")
                .queryAll()
                .stream()
                .filter(node -> {
                    @SuppressWarnings("rawtypes")
                    ComboBox comboBox = (ComboBox) node;

                    return comboBox.getItems().stream()
                            .anyMatch(item ->
                                    item instanceof com.edujournal.model.UserDTO);
                })
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError("Teacher ComboBox not found")
                );

        clickOn(teacherComboBox);
        sleep(500);

        clickOn("Anna Korhonen");
        sleep(500);

        Node groupComboBox = lookup(".combo-box")
                .queryAll()
                .stream()
                .filter(node -> {
                    @SuppressWarnings("rawtypes")
                    ComboBox comboBox = (ComboBox) node;

                    return comboBox.getItems().stream()
                            .anyMatch(item ->
                                    item instanceof com.edujournal.model.AcademicGroupDTO);
                })
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError("Group ComboBox not found")
                );

        interact(() -> {
            @SuppressWarnings("rawtypes")
            ComboBox comboBox = (ComboBox) groupComboBox;

            for (Object item : comboBox.getItems()) {
                if (item instanceof com.edujournal.model.AcademicGroupDTO group
                        && "E2ECourse Group".equals(group.getName())) {

                    comboBox.getSelectionModel().select(item);
                    return;
                }
            }

            throw new AssertionError(
                    "E2ECourse Group not found in group ComboBox"
            );
        });

        waitForFxEvents();

        DatePicker startDatePicker =
                lookup(".date-picker").nth(0)
                        .queryAs(DatePicker.class);

        DatePicker endDatePicker =
                lookup(".date-picker").nth(1)
                        .queryAs(DatePicker.class);

        interact(() -> {
            startDatePicker.setValue(LocalDate.of(2026, 9, 1));
            endDatePicker.setValue(LocalDate.of(2027, 6, 30));
        });

        clickOn("Save");
        sleep(1200);

        assertTrue(
                lookup("E2E Course").tryQuery().isPresent(),
                "Created course should appear in the course list"
        );
    }

    @Test
    void adminCanCompleteFullCreationFlow() {
        String studentFirstName = "E2EFullStudent";
        String studentLastName = "TestUser";

        String teacherFirstName = "E2EFullTeacher";
        String teacherLastName = "TestUser";

        String groupName = "E2E Full Group";

        String courseName = "E2E Full Course";
        String courseCode = "E2EFULL101";

        sleep(1000);

        clickOn(".text-field");
        write("admin");

        clickOn(".password-field");
        write("demo_password");

        clickOn("Sign in");
        sleep(1000);

        clickOn("Add student");
        sleep(500);

        clickOn("First Name");
        write(studentFirstName);

        clickOn("Last Name");
        write(studentLastName);

        push(KeyCode.TAB);
        write("5551234567");

        clickOn("Save");
        sleep(1000);

        assertTrue(
                lookup(studentFirstName).tryQuery().isPresent(),
                "Created student should appear in the student list"
        );

        clickOn("Dashboard");
        sleep(1000);

        clickOn("Add teacher");
        sleep(500);

        clickOn("First Name");
        write(teacherFirstName);

        clickOn("Last Name");
        write(teacherLastName);

        push(KeyCode.TAB);
        write("5551112233");

        clickOn("Save");
        sleep(1000);

        assertTrue(
                lookup(teacherFirstName).tryQuery().isPresent(),
                "Created teacher should appear in the teacher list"
        );

        clickOn("Dashboard");
        sleep(1000);

        clickOn("Add group");
        sleep(500);

        clickOn("#groupNameField");
        write(groupName);

        clickOn("Save");
        sleep(1200);

        assertTrue(
                lookup(groupName).tryQuery().isPresent(),
                "Created academic group should appear in the group list"
        );

        clickOn("Dashboard");
        sleep(1000);

        clickOn("Add course");
        sleep(500);

        clickOn("#courseNameField");
        write(courseName);

        clickOn("#courseCodeField");
        write(courseCode);

        selectTeacher(
                teacherFirstName,
                teacherLastName
        );

        selectGroup(groupName);

        DatePicker startDatePicker =
                lookup(".date-picker").nth(0)
                        .queryAs(DatePicker.class);

        DatePicker endDatePicker =
                lookup(".date-picker").nth(1)
                        .queryAs(DatePicker.class);

        interact(() -> {
            startDatePicker.setValue(LocalDate.of(2026, 9, 1));
            endDatePicker.setValue(LocalDate.of(2027, 6, 30));
        });
        clickOn("Save");
        sleep(1000);

        assertTrue(
                lookup(courseName).tryQuery().isPresent(),
                "Created course should appear in the course list"
        );
    }

    @Test
    void adminCanChangeLanguage() {
        sleep(1200);

        TextField username = lookup(".text-field").queryAs(TextField.class);
        PasswordField password = lookup(".password-field").queryAs(PasswordField.class);

        interact(() -> {
            username.setText("admin");
            password.setText("demo_password");
        });

        clickOn("Sign in");
        sleep(1200);

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

    private void selectTeacher(String firstName, String lastName) {
        Node node = lookup(".combo-box")
                .queryAll()
                .stream()
                .filter(candidate -> {
                    @SuppressWarnings("rawtypes")
                    ComboBox comboBox = (ComboBox) candidate;

                    return comboBox.getItems().stream()
                            .anyMatch(item ->
                                    item instanceof com.edujournal.model.UserDTO);
                })
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError("Teacher ComboBox not found")
                );

        interact(() -> {
            @SuppressWarnings("rawtypes")
            ComboBox comboBox = (ComboBox) node;

            for (Object item : comboBox.getItems()) {
                if (item instanceof com.edujournal.model.UserDTO user) {
                    String fullName =
                            user.getFirstName() + " " + user.getLastName();

                    System.out.println(
                            "[TEST] Teacher option: " + fullName
                    );

                    if (fullName.equals(firstName + " " + lastName)) {
                        comboBox.getSelectionModel().select(item);
                        return;
                    }
                }
            }

            throw new AssertionError(
                    "Teacher not found: "
                            + firstName + " " + lastName
            );
        });

        waitForFxEvents();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void selectGroup(String groupName) {
        Node groupComboBox = lookup(".combo-box")
                .queryAll()
                .stream()
                .filter(node -> {
                    ComboBox comboBox = (ComboBox) node;

                    return comboBox.getItems().stream()
                            .anyMatch(item ->
                                    item instanceof com.edujournal.model.AcademicGroupDTO);
                })
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError("Group ComboBox not found")
                );

        interact(() -> {
            ComboBox comboBox = (ComboBox) groupComboBox;

            for (Object item : comboBox.getItems()) {
                if (item instanceof com.edujournal.model.AcademicGroupDTO group) {
                    String name = group.getName();

                    System.out.println(
                            "[TEST] Group option: " + name
                    );

                    if (groupName.equals(name)) {
                        comboBox.getSelectionModel().select(item);
                        return;
                    }
                }
            }

            throw new AssertionError(
                    "Group not found: " + groupName
            );
        });

        waitForFxEvents();
    }
}