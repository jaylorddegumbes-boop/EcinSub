package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {

    private final UserDao userDao;
    private final Scanner scanner;

    public AdminMenu(UserDao userDao, Scanner scanner){
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void display(User admin) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n========================================");
            System.out.println("          ADMIN DASHBOARD              ");
            System.out.println("========================================");
            System.out.println("1. View All Users");
            System.out.println("2. View Pending User Approvals");
            System.out.println("3. Approve User Registration");
            System.out.println("4. Add New User");
            System.out.println("5. Update User Role / Status");
            System.out.println("6. Delete User Account");
            System.out.println("7. Logout");
            System.out.print("Choose an option (1-7): ");

            String choice = scanner.nextLine().trim();

            switch (choice){
                case "1" -> viewAllUser();
                case "2" -> viewPendingUsers();
                case "3" -> approveUser();
                case "7" -> {
                    loggedIn = false;
                    System.out.println("Logging out...");
                }
                default -> System.out.println(" -> [ERROR] invalid Option.");
            }
        }
    }

    private void displayUserTable(List<User> users, String title){
        System.out.println("\n==================" + title + "==================");
        if(users.isEmpty()){
            System.out.println("-> No Records Found");
            return;
        }
        System.out.println("+-----+-----------------+------------+------------+");
        System.out.printf("| %-3s | %-15s | %-10s | %-10s |\n", "ID", "Username", "Role", "Status");
        System.out.println("+-----+-----------------+------------+------------+");
        for(User u : users){
            System.out.printf("| %-3s | %-15s | %-10s | %-10s |\n",
                    u.id(), u.username(), u.roles(), u.status());
        }
        System.out.println("+-----+-----------------+------------+------------+");
    }

    private void viewAllUser(){
        displayUserTable(userDao.getAllUser(), "ALL REGISTERED USER");
    }

    private void viewPendingUsers(){
        displayUserTable(userDao.getPendingUser(), "PENDING APPROVALS");
    }

    private void approveUser(){
        List<User> pendingUsers = userDao.getPendingUser();
        displayUserTable(pendingUsers, "PENDING APPROVALS");

        if(pendingUsers.isEmpty()){
            return;
        }

        int userId = readIntInput("Enter User ID to approve (or 0 to cancel): ");
        if (userId == 0){
            System.out.println(" -> Operation Cancelled by the user.");
            return; // Stops execution so it doesn't search for ID 0
        }

        boolean exists = pendingUsers.stream().anyMatch(u -> u.id() == userId);
        if(!exists){
            System.out.println(" -> [ERROR] User ID not found.");
            return;
        }

        userDao.approveUser(userId);
    }

    // Helper method to safely read integer inputs from the console
    private int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(" -> [ERROR] Invalid input. Please enter a valid number.");
            }
        }
    }
}