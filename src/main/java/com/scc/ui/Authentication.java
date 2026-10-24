package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.Scanner;

public class Authentication {

    private final UserDao userDao;
    private final Scanner sc;

    public Authentication(UserDao userDao, Scanner sc) {
        this.userDao = userDao;
        this.sc = sc;
    }

    public void start() {
        boolean running = true;

        while (running) {

            System.out.println("\n======================================");
            System.out.println(" ECINREV: SUBDIVISION VISITOR ENTRY & EXIT MONITORING SYSTEM");
            System.out.println("======================================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    handleLogin();
                    break;

                case "2":
                    System.out.print("Enter new username: ");
                    String user = sc.nextLine();
                    System.out.print("Enter password: ");
                    String pass = sc.nextLine();

                    System.out.print("Select Role [1] SECURITY_GUARD | [2] ADMIN | [3] RESIDENT | [4] SUPERVISOR: ");
                    int roleChoice = Integer.parseInt(sc.nextLine().trim());
                    String role;

                    switch (roleChoice) {
                        case 1:
                            role = "SECURITY_GUARD";
                            break;
                        case 2:
                            role = "ADMIN";
                            break;
                        case 3:
                            role = "RESIDENT";
                            break;
                        case 4:
                            role = "SUPERVISOR";
                            break;
                        default:
                            role = null;
                    }

                    if (role != null) {
                        boolean success = userDao.registerUser(user, pass, role);
                        if (success) {
                            System.out.println("Registration successful!");
                        }
                    } else {
                        System.out.println("Invalid role selection!");
                    }
                    break;

                case "3":
                    running = false;
                    System.out.println("Exiting system...");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void handleLogin() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        User user = userDao.login(username, password);

        if (user != null) {
            System.out.println("\n -> Welcome " + user.username() + " | ROLE: " + user.roles());
            switch (user.roles()) {
                case "ADMIN" -> new AdminMenu(userDao, sc).display(user);
                default -> System.out.println("Account roles not found!");
            }
        } else {
            System.out.println("Invalid credentials!");
        }
    }
}