package com.edujournal;

import com.edujournal.dao.UserDAO;
import com.edujournal.entity.User;
import com.edujournal.database.DatabaseInitializer;

public class DatabaseTest {

    private static final String TEST_USERNAME = "Test_user_crud_test_from_app";

    public static void main(String[] args) {

        DatabaseInitializer.initialize();
        UserDAO userDAO = new UserDAO();

        testCreate(userDAO);
        testRead(userDAO);
        testUpdate(userDAO);
        testDelete(userDAO);
        testRead(userDAO);
    }

    public static void testCreate(UserDAO userDAO) {

        System.out.println("\n--- CREATE TEST ---");

        User user = new User();

        user.setUsername(TEST_USERNAME);
        user.setPasswordHash("test_password");
        user.setFirstName("CRUD");
        user.setLastName("Test");
        user.setRole("STUDENT");

        userDAO.save(user);

        User createdUser = userDAO.findByUsername(TEST_USERNAME);

        if (createdUser != null
                && TEST_USERNAME.equals(createdUser.getUsername())
                && "CRUD".equals(createdUser.getFirstName())
                && "Test".equals(createdUser.getLastName())
                && "STUDENT".equals(createdUser.getRole())) {

            System.out.println("CREATE: PASS");
            System.out.println("User successfully created in database.");
            System.out.println("ID: " + createdUser.getId());
            /*
            System.out.println("Username: " + createdUser.getUsername());
            System.out.println("Name: "
                    + createdUser.getFirstName()
                    + " "
                    + createdUser.getLastName());

             */
            System.out.println("Role: " + createdUser.getRole());
        } else {
            System.out.println("CREATE: FAIL");
            System.out.println("User was not created correctly.");
        }
    }

    public static void testRead(UserDAO userDAO) {

        System.out.println("\n--- READ TEST ---");

        User foundUser = userDAO.findByUsername(TEST_USERNAME);

        if (foundUser != null) {

            System.out.println("READ: PASS");
            System.out.println("User found successfully!");
            System.out.println("ID: " + foundUser.getId());
            /*
            System.out.println("Username: " + foundUser.getUsername());
            System.out.println("Name: "
                    + foundUser.getFirstName()
                    + " "
                    + foundUser.getLastName());
            System.out.println("Role: " + foundUser.getRole());
            */
        } else {

            System.out.println("READ: FAIL");
            System.out.println("User not found.");
        }
    }

    public static void testUpdate(UserDAO userDAO) {

        System.out.println("\n--- UPDATE TEST ---");

        User user = userDAO.findByUsername(TEST_USERNAME);

        if (user == null) {

            System.out.println("UPDATE: FAIL");
            System.out.println("User not found.");
            return;
        }

        user.setFirstName("Updated");
        user.setLastName("User");
        user.setRole("TEACHER");

        userDAO.update(user);

        User updatedUser = userDAO.findByUsername(TEST_USERNAME);

        if (updatedUser != null
                && "Updated".equals(updatedUser.getFirstName())
                && "User".equals(updatedUser.getLastName())
                && "TEACHER".equals(updatedUser.getRole())) {

            System.out.println("UPDATE: PASS");
            System.out.println("User updated successfully!");
           /*
            System.out.println("Updated name: "
                    + updatedUser.getFirstName()
                    + " "
                    + updatedUser.getLastName());

            */
            System.out.println("Updated role: "
                    + updatedUser.getRole());
        } else {
            System.out.println("UPDATE: FAIL");
            System.out.println("User was not updated correctly.");
        }
    }

    public static void testDelete(UserDAO userDAO) {

        System.out.println("\n--- DELETE TEST ---");

        User user = userDAO.findByUsername(TEST_USERNAME);

        if (user == null) {

            System.out.println("DELETE: FAIL");
            System.out.println("User not found.");
            return;
        }

        Integer deletedUserId = user.getId();

        userDAO.delete(user);
        User deletedUser = userDAO.findByUsername(TEST_USERNAME);

        if (deletedUser == null) {
            System.out.println("DELETE: PASS");
            System.out.println("User deleted successfully!");
            System.out.println("Deleted user ID: " + deletedUserId);

        } else {
            System.out.println("DELETE: FAIL");
            System.out.println("User still exists in database.");
        }
    }
}