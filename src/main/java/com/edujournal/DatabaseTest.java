package com.edujournal;

import com.edujournal.dao.UserDAO;
import com.edujournal.entity.User;
import com.edujournal.database.DatabaseInitializer;

public class DatabaseTest {

    public static void main(String[] args) {

        DatabaseInitializer.initialize();

        try {
            UserDAO userDAO = new UserDAO();

            User user = userDAO.findByUsername("admin");

            if (user != null) {
                System.out.println("USER FOUND!");
                System.out.println("Username: " + user.getUsername());
                System.out.println("Name: " + user.getFirstName() + " " + user.getLastName());
                System.out.println("Role: " + user.getRole());
            } else {
                System.out.println("USER NOT FOUND!");
            }

        } catch (Exception e) {
            System.out.println("DATABASE TEST FAILED!");
            e.printStackTrace();
        }
    }
}