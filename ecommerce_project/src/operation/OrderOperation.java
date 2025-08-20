package operation;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Order;

public class OrderOperation {
    private static OrderOperation instance;
    private static final String ORDER_FILE = "data/orders.txt";

    private OrderOperation() {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();
    }

    public static OrderOperation getInstance() {
        if (instance == null) {
            instance = new OrderOperation();
        }
        return instance;
    }

    public String generateUniqueOrderId() {
        String id;
        Random random = new Random();
        do {
            id = "o_" + String.format("%05d", random.nextInt(100000));
        } while (checkOrderIdExists(id));
        return id;
    }

    private boolean checkOrderIdExists(String orderId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"order_id\":\"" + orderId + "\"")) {
                    return true;
                }
            }
        } catch (IOException e) {
        }
        return false;
    }

    public OrderListResult getOrderList(String customerId, int pageNumber) {
        List<Order> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (customerId == null || line.contains("\"user_id\":\"" + customerId + "\"")) {
                    String[] parts = line.split(",\\s*");
                    String orderId = "", userId = "", proId = "", orderTime = "";
                    for (String part : parts) {
                        if (part.contains("\"order_id\":")) orderId = part.split(":")[1].replaceAll("[\"}]", "");
                        else if (part.contains("\"user_id\":")) userId = part.split(":")[1].replaceAll("[\"}]", "");
                        else if (part.contains("\"pro_id\":")) proId = part.split(":")[1].replaceAll("[\"}]", "");
                        else if (part.contains("\"order_time\":")) orderTime = part.split(":")[1].replaceAll("[\"}]", "");
                    }
                    orders.add(new Order(orderId, userId, proId, orderTime));
                }
            }
        } catch (IOException e) {
        }
        int totalPages = (int) Math.ceil(orders.size() / 10.0);
        int start = (pageNumber - 1) * 10;
        int end = Math.min(start + 10, orders.size());
        return new OrderListResult(orders.subList(start, end), pageNumber, totalPages);
    }

    public void deleteAllOrders() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDER_FILE))) {
            writer.write("");
        } catch (IOException e) {
        }
    }
}