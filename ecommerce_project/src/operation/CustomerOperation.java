package operation;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import model.Customer;

public class CustomerOperation {
    private static CustomerOperation instance;
    private static final String USER_FILE = "data/users.txt";

    private CustomerOperation() {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();
    }

    public static CustomerOperation getInstance() {
        if (instance == null) {
            instance = new CustomerOperation();
        }
        return instance;
    }

    public boolean validateEmail(String userEmail) {
        return Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", userEmail);
    }

    public boolean validateMobile(String userMobile) {
        return Pattern.matches("^0[34]\\d{8}$", userMobile);
    }

    public boolean registerCustomer(String userName, String userPassword, String userEmail, String userMobile) {
        UserOperation userOp = UserOperation.getInstance();
        if (!userOp.validateUsername(userName) || userOp.checkUsernameExist(userName) || !userOp.validatePassword(userPassword) ||
            !validateEmail(userEmail) || !validateMobile(userMobile)) {
            return false;
        }
        String userId = userOp.generateUniqueUserId();
        String encryptedPassword = userOp.encryptPassword(userPassword);
        String registerTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss"));
        Customer customer = new Customer(userId, userName, encryptedPassword, registerTime, "customer", userEmail, userMobile);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            writer.write(customer.toString());
            writer.newLine();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean updateProfile(String attributeName, String value, Customer customerObject) {
        UserOperation userOp = UserOperation.getInstance();
        switch (attributeName) {
            case "username":
                if (!userOp.validateUsername(value) || (userOp.checkUsernameExist(value) && !value.equals(customerObject.getUserName()))) {
                    return false;
                }
                customerObject.setUserName(value);
                break;
            case "password":
                if (!userOp.validatePassword(value)) {
                    return false;
                }
                customerObject.setUserPassword(userOp.encryptPassword(value));
                break;
            case "email":
                if (!validateEmail(value)) {
                    return false;
                }
                customerObject.setUserEmail(value);
                break;
            case "mobile":
                if (!validateMobile(value)) {
                    return false;
                }
                customerObject.setUserMobile(value);
                break;
            default:
                return false;
        }
        return updateCustomerInFile(customerObject);
    }

    private boolean updateCustomerInFile(Customer customer) {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"user_id\":\"" + customer.getUserId() + "\"")) {
                    lines.add(customer.toString());
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            return false;
        }
        if (!found) return false;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public CustomerListResult getCustomerList(int pageNumber) {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"user_role\":\"customer\"")) {
                    String[] parts = line.split(",\\s*");
                    String userId = "", userName = "", userPassword = "", userRegisterTime = "", userEmail = "", userMobile = "";
                    for (String part : parts) {
                        if (part.contains("\"user_id\":")) userId = part.split(":")[1].replaceAll("[\"}]", "");
                        else if (part.contains("\"user_name\":")) userName = part.split(":")[1].replaceAll("[\"}]", "");
                        else if (part.contains("\"user_password\":")) userPassword = part.split(":")[1].replaceAll("[\"}]", "");
                        else if (part.contains("\"user_register_time\":")) userRegisterTime = part.split(":")[1].replaceAll("[\"}]", "");
                        else if (part.contains("\"user_email\":")) userEmail = part.split(":")[1].replaceAll("[\"}]", "");
                        else if (part.contains("\"user_mobile\":")) userMobile = part.split(":")[1].replaceAll("[\"}]", "");
                    }
                    customers.add(new Customer(userId, userName, userPassword, userRegisterTime, "customer", userEmail, userMobile));
                }
            }
        } catch (IOException e) {
        }
        int totalPages = (int) Math.ceil(customers.size() / 10.0);
        int start = (pageNumber - 1) * 10;
        int end = Math.min(start + 10, customers.size());
        return new CustomerListResult(customers.subList(start, end), pageNumber, totalPages);
    }

    public void deleteAllCustomers() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains("\"user_role\":\"customer\"")) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
        }
    }
}