
package com.scc;

import com.scc.dao.UserDao;
import com.scc.model.User;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserDao userDao = new UserDao();

        boolean running = true;

        while (running) {

            System.out.println("\n======================================");
            System.out.println(" ECINREV: SUBDIVISION VISITOR ENTRY & EXIT MONITORING SYSTEM");
            System.out.println("======================================");
            System.out.println("1. Register Account");
            System.out.println("2. Login");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            String choice = sc.nextLine().trim();

            switch (choice) {

                // REGISTER ACCOUNT
                case "1" -> {
                    System.out.print("Enter username: ");
                    String username = sc.nextLine().trim();

                    System.out.print("Enter password: ");
                    String password = sc.nextLine();

                    System.out.println("\nSelect User Role:");
                    System.out.println("1. SECURITY_GUARD");
                    System.out.println("2. ADMIN");
                    System.out.println("3. RESIDENT");
                    System.out.println("4. SUPERVISOR");
                    System.out.print("Choose role: ");

                    String roleChoice = sc.nextLine().trim();

                    String role = switch (roleChoice) {
                        case "1" -> "SECURITY_GUARD";
                        case "2" -> "ADMIN";
                        case "3" -> "RESIDENT";
                        case "4" -> "SUPERVISOR";
                        default -> null;
                    };

                    if (username.isBlank() || password.isBlank()) {
                        System.out.println(
                                "Username and password are required."
                        );
                        break;
                    }

                    if (role == null) {
                        System.out.println("Invalid role selection.");
                        break;
                    }

                    userDao.registerUser(
                            username, password, role
                    );
                }

                // LOGIN
                case "2" -> {
                    System.out.print("Enter username: ");
                    String username = sc.nextLine().trim();

                    System.out.print("Enter password: ");
                    String password = sc.nextLine();

                    User user = userDao.login(
                            username, password
                    );

                    if (user != null) {
                        System.out.println(
                                "\nWelcome, " + user.username() + "!"
                        );
                        System.out.println(
                                "Role: " + user.roles()
                        );

                        boolean loggedIn = true;

                        while (loggedIn && running) {
                            System.out.println("\n========== USER MENU ==========");
                            System.out.println("1. Logout");
                            System.out.println("0. Exit Application");
                            System.out.print("Choose: ");

                            String action = sc.nextLine().trim();

                            switch (action) {
                                case "1" -> {
                                    System.out.println(
                                            "Logged out successfully."
                                    );
                                    loggedIn = false;
                                }

                                case "0" -> {
                                    System.out.println(
                                            "Thank you for using ECINREV. Goodbye!"
                                    );
                                    loggedIn = false;
                                    running = false;
                                }

                                default -> System.out.println(
                                        "Invalid choice. Please try again."
                                );
                            }
                        }
                    }
                }

                // EXIT
                case "0" -> {
                    System.out.println(
                            "Thank you for using ECINREV. Goodbye!"
                    );
                    running = false;
                }

                default -> System.out.println(
                        "Invalid choice. Please try again."
                );
            }
        }

        sc.close();
    }
}
