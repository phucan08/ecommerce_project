package io;

import java.util.List;
import java.util.Scanner;

public class IOInterface {
    private static IOInterface instance;
    private Scanner scanner;

    private IOInterface() {
        scanner = new Scanner(System.in);
    }

    public static IOInterface getInstance() {
        if (instance == null) {
            instance = new IOInterface();
        }
        return instance;
    }

    public String[] getUserInput(String message, int numOfArgs) {
        printMessage(message);
        String[] inputs = scanner.nextLine().trim().split("\\s+", numOfArgs);
        String[] result = new String[numOfArgs];
        for (int i = 0; i < numOfArgs; i++) {
            result[i] = i < inputs.length ? inputs[i] : "";
        }
        return result;
    }

    public void mainMenu() {
        printMessage("=== E-Commerce System ===");
        printMessage("1. Login");
        printMessage("2. Register");
        printMessage("3. Quit");
    }

    public void adminMenu() {
        printMessage("=== Admin Menu ===");
        printMessage("1. Show products");
        printMessage("2. Add customers");
        printMessage("3. Show customers");
        printMessage("4. Show orders");
        printMessage("5. Generate test data");
        printMessage("6. Generate all statistical figures");
        printMessage("7. Delete all data");
        printMessage("8. Logout");
    }

    public void customerMenu() {
        printMessage("=== Customer Menu ===");
        printMessage("1. Show profile");
        printMessage("2. Update profile");
        printMessage("3. Show products");
        printMessage("4. Show history orders");
        printMessage("5. Generate all consumption figures");
        printMessage("6. Logout");
    }

    public void showList(String userRole, String listType, List<?> objectList, int pageNumber, int totalPages) {
        printMessage("=== " + listType + " List (Page " + pageNumber + "/" + totalPages + ") ===");
        int row = 1;
        for (Object obj : objectList) {
            printMessage(row + ". " + obj.toString());
            row++;
        }
    }

    public void printErrorMessage(String errorSource, String errorMessage) {
        printMessage("Error in " + errorSource + ": " + errorMessage);
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printObject(Object targetObject) {
        printMessage(targetObject.toString());
    }
}