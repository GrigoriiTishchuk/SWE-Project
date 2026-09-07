package com.edujournal.database;

import com.edujournal.dao.UserDAO;
import com.edujournal.entity.User;

public class DatabaseTest {

    public static void main(String[] args) {

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