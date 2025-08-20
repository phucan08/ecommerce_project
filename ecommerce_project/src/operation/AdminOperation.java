package operation;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import model.Admin;

public class AdminOperation {
    private static AdminOperation instance;
    private static final String USER_FILE = "data/users.txt";

    private AdminOperation() {}

    public static AdminOperation getInstance() {
        if (instance == null) {
            instance = new AdminOperation();
        }
        return instance;
    }

    public void registerAdmin() {
        UserOperation userOp = UserOperation.getInstance();

        // Remove existing admin if exists
        if (userOp.checkUsernameExist("admin")) {
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.contains("\"user_name\":\"admin\"")) {
                        lines.add(line);
                    }
                }
            } catch (IOException e) {
                // Handle exception silently
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                // Handle exception silently
            }
        }

        // Create new admin
        String userId = userOp.generateUniqueUserId();
        String encryptedPassword = userOp.encryptPassword("admin123");
        String registerTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss"));
        Admin admin = new Admin(userId, "admin", encryptedPassword, registerTime, "admin");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            writer.write(admin.toString());
            writer.newLine();
        } catch (IOException e) {
            // Handle exception silently
        }
    }
}