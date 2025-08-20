import io.IOInterface;
import java.util.List;
import model.Customer;
import model.Product;
import model.User;
import operation.AdminOperation;
import operation.CustomerListResult;
import operation.CustomerOperation;
import operation.OrderListResult;
import operation.OrderOperation;
import operation.ProductListResult;
import operation.ProductOperation;
import operation.UserOperation;

public class Main {
    public static void main(String[] args) {
        IOInterface io = IOInterface.getInstance();
        UserOperation userOp = UserOperation.getInstance();
        AdminOperation adminOp = AdminOperation.getInstance();
        boolean running = true;

        adminOp.registerAdmin();

        while (running) {
            io.mainMenu();
            String[] input = io.getUserInput("Enter choice: ", 1);
            String choice = input[0];

            switch (choice) {
                case "1":
                    String[] loginInput = io.getUserInput("Enter username and password: ", 2);
                    String username = loginInput[0];
                    String password = loginInput[1];
                    User user = userOp.login(username, password);
                    if (user == null) {
                        io.printErrorMessage("Login", "Invalid username or password");
                        continue;
                    }
                    if (user.getUserRole().equals("admin")) {
                        handleAdminMenu(io, user);
                    } else if (user.getUserRole().equals("customer")) {
                        handleCustomerMenu(io, (Customer) user);
                    }
                    break;

                case "2":
                    String[] regInput = io.getUserInput("Enter username, password, email, mobile: ", 4);
                    boolean success = CustomerOperation.getInstance().registerCustomer(regInput[0], regInput[1], regInput[2], regInput[3]);
                    if (success) {
                        io.printMessage("Registration successful");
                    } else {
                        io.printErrorMessage("Registration", "Invalid input or username exists");
                    }
                    break;

                case "3":
                    running = false;
                    io.printMessage("Exiting system");
                    break;

                default:
                    io.printErrorMessage("Main Menu", "Invalid choice");
            }
        }
    }

    private static void handleAdminMenu(IOInterface io, User user) {
        boolean adminRunning = true;
        while (adminRunning) {
            io.adminMenu();
            String[] input = io.getUserInput("Enter choice: ", 1);
            String choice = input[0];

            switch (choice) {
                case "1":
                    ProductListResult productResult = ProductOperation.getInstance().getProductList(1);
                    io.showList("admin", "Product", productResult.getList(), productResult.getCurrentPage(), productResult.getTotalPages());
                    break;

                case "2":
                    String[] custInput = io.getUserInput("Enter username, password, email, mobile: ", 4);
                    boolean success = CustomerOperation.getInstance().registerCustomer(custInput[0], custInput[1], custInput[2], custInput[3]);
                    if (success) {
                        io.printMessage("Customer added successfully");
                    } else {
                        io.printErrorMessage("Add Customer", "Invalid input or username exists");
                    }
                    break;

                case "3":
                    CustomerListResult customerResult = CustomerOperation.getInstance().getCustomerList(1);
                    io.showList("admin", "Customer", customerResult.getList(), customerResult.getCurrentPage(), customerResult.getTotalPages());
                    break;

                case "4":
                    OrderListResult orderResult = OrderOperation.getInstance().getOrderList(null, 1);
                    io.showList("admin", "Order", orderResult.getList(), orderResult.getCurrentPage(), orderResult.getTotalPages());
                    break;

                case "7":
                    CustomerOperation.getInstance().deleteAllCustomers();
                    ProductOperation.getInstance().deleteAllProducts();
                    OrderOperation.getInstance().deleteAllOrders();
                    io.printMessage("All data deleted");
                    break;

                case "8":
                    adminRunning = false;
                    io.printMessage("Logged out");
                    break;

                default:
                    io.printErrorMessage("Admin Menu", "Invalid choice");
            }
        }
    }

    private static void handleCustomerMenu(IOInterface io, Customer customer) {
        boolean customerRunning = true;
        while (customerRunning) {
            io.customerMenu();
            String[] input = io.getUserInput("Enter choice (e.g., '3 keyword' or '3'): ", 2);
            String choice = input[0];

            switch (choice) {
                case "1":
                    io.printObject(customer);
                    break;

                case "2":
                    String[] updateInput = io.getUserInput("Enter attribute (username/password/email/mobile) and new value: ", 2);
                    boolean updated = CustomerOperation.getInstance().updateProfile(updateInput[0], updateInput[1], customer);
                    if (updated) {
                        io.printMessage("Profile updated successfully");
                    } else {
                        io.printErrorMessage("Update Profile", "Invalid attribute or value");
                    }
                    break;

                case "3":
                    List<Product> products;
                    if (!input[1].isEmpty()) {
                        products = ProductOperation.getInstance().getProductListByKeyword(input[1]);
                    } else {
                        ProductListResult result = ProductOperation.getInstance().getProductList(1);
                        products = result.getList();
                    }
                    io.showList("customer", "Product", products, 1, 1);
                    break;

                case "4":
                    OrderListResult orderResult = OrderOperation.getInstance().getOrderList(customer.getUserId(), 1);
                    io.showList("customer", "Order", orderResult.getList(), orderResult.getCurrentPage(), orderResult.getTotalPages());
                    break;

                case "6":
                    customerRunning = false;
                    io.printMessage("Logged out");
                    break;

                default:
                    io.printErrorMessage("Customer Menu", "Invalid choice");
            }
        }
    }
}