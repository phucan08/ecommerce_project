package operation;

import java.io.*;
import java.util.Random;
import java.util.regex.Pattern;
import model.Admin;
import model.Customer;
import model.User;

public class UserOperation {
    private static UserOperation instance;
    private static final String USER_FILE = "data/users.txt";

    private UserOperation() {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();
    }

    public static UserOperation getInstance() {
        if (instance == null) {
            instance = new UserOperation();
        }
        return instance;
    }

    public String generateUniqueUserId() {
        String id;
        do {
            id = "u_" + String.format("%010d", new Random().nextInt(1000000000));
        } while (checkUserIdExists(id));
        return id;
    }

    private boolean checkUserIdExists(String userId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"user_id\":\"" + userId + "\"")) {
                    return true;
                }
            }
        } catch (IOException e) {
        }
        return false;
    }

    public String encryptPassword(String userPassword) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder randomStr = new StringBuilder();
        for (int i = 0; i < userPassword.length() * 2; i++) {
            randomStr.append(chars.charAt(random.nextInt(chars.length())));
        }
        StringBuilder encrypted = new StringBuilder("^^");
        for (int i = 0; i < userPassword.length(); i++) {
            encrypted.append(randomStr.charAt(i * 2)).append(randomStr.charAt(i * 2 + 1)).append(userPassword.charAt(i));
        }
        encrypted.append("$$");
        return encrypted.toString();
    }

    public String decryptPassword(String encryptedPassword) {
        if (!encryptedPassword.startsWith("^^") || !encryptedPassword.endsWith("$$")) {
            return "";
        }
        String trimmed = encryptedPassword.substring(2, encryptedPassword.length() - 2);
        StringBuilder decrypted = new StringBuilder();
        for (int i = 2; i < trimmed.length(); i += 3) {
            decrypted.append(trimmed.charAt(i));
        }
        return decrypted.toString();
    }

    public boolean checkUsernameExist(String userName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"user_name\":\"" + userName + "\"")) {
                    return true;
                }
            }
        } catch (IOException e) {
        }
        return false;
    }

    public boolean validateUsername(String userName) {
        return userName != null && userName.matches("^[a-zA-Z_]{5,}$");
    }

    public boolean validatePassword(String userPassword) {
        return userPassword != null && userPassword.length() >= 5 &&
                Pattern.compile("[a-zA-Z]").matcher(userPassword).find() &&
                Pattern.compile("[0-9]").matcher(userPassword).find();
    }

    public User login(String userName, String userPassword) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"user_name\":\"" + userName + "\"")) {
                    String[] parts = line.split(",\\s*");
                    String encryptedPassword = "";
                    String userId = "", userRegisterTime = "", userRole = "", userEmail = "", userMobile = "";
                    for (String part : parts) {
                        if (part.contains("\"user_password\":")) {
                            encryptedPassword = part.split(":")[1].replaceAll("[\"}]", "");
                        } else if (part.contains("\"user_id\":")) {
                            userId = part.split(":")[1].replaceAll("[\"}]", "");
                        } else if (part.contains("\"user_register_time\":")) {
                            userRegisterTime = part.split(":")[1].replaceAll("[\"}]", "");
                        } else if (part.contains("\"user_role\":")) {
                            userRole = part.split(":")[1].replaceAll("[\"}]", "");
                        } else if (part.contains("\"user_email\":")) {
                            userEmail = part.split(":")[1].replaceAll("[\"}]", "");
                        } else if (part.contains("\"user_mobile\":")) {
                            userMobile = part.split(":")[1].replaceAll("[\"}]", "");
                        }
                    }
                    if (decryptPassword(encryptedPassword).equals(userPassword)) {
                        if (userRole.equals("admin")) {
                            return new Admin(userId, userName, encryptedPassword, userRegisterTime, userRole);
                        } else {
                            return new Customer(userId, userName, encryptedPassword, userRegisterTime, userRole, userEmail, userMobile);
                        }
                    }
                }
            }
        } catch (IOException e) {
        }
        return null;
    }
}