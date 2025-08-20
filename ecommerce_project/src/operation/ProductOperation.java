package operation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import model.Product;

public class ProductOperation {
    private static ProductOperation instance;
    private static final String PRODUCT_FILE = "data/products.txt";

    private ProductOperation() {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();
    }

    public static ProductOperation getInstance() {
        if (instance == null) {
            instance = new ProductOperation();
        }
        return instance;
    }

    public ProductListResult getProductList(int pageNumber) {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",\\s*");
                String proId = "", proModel = "", proCategory = "", proName = "";
                double proCurrentPrice = 0.0, proRawPrice = 0.0, proDiscount = 0.0;
                int proLikesCount = 0;
                for (String part : parts) {
                    if (part.contains("\"pro_id\":")) proId = part.split(":")[1].replaceAll("[\"}]", "");
                    else if (part.contains("\"pro_model\":")) proModel = part.split(":")[1].replaceAll("[\"}]", "");
                    else if (part.contains("\"pro_category\":")) proCategory = part.split(":")[1].replaceAll("[\"}]", "");
                    else if (part.contains("\"pro_name\":")) proName = part.split(":")[1].replaceAll("[\"}]", "");
                    else if (part.contains("\"pro_current_price\":")) proCurrentPrice = Double.parseDouble(part.split(":")[1].replaceAll("[\"}]", ""));
                    else if (part.contains("\"pro_raw_price\":")) proRawPrice = Double.parseDouble(part.split(":")[1].replaceAll("[\"}]", ""));
                    else if (part.contains("\"pro_discount\":")) proDiscount = Double.parseDouble(part.split(":")[1].replaceAll("[\"}]", ""));
                    else if (part.contains("\"pro_likes_count\":")) proLikesCount = Integer.parseInt(part.split(":")[1].replaceAll("[\"}]", ""));
                }
                products.add(new Product(proId, proModel, proCategory, proName, proCurrentPrice, proRawPrice, proDiscount, proLikesCount));
            }
        } catch (IOException e) {
        }
        int totalPages = (int) Math.ceil(products.size() / 10.0);
        int start = (pageNumber - 1) * 10;
        int end = Math.min(start + 10, products.size());
        return new ProductListResult(products.subList(start, end), pageNumber, totalPages);
    }

    public List<Product> getProductListByKeyword(String keyword) {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(keyword.toLowerCase())) {
                    String[] parts = line.split(",\\s*");
                    String proId = "", proModel = "", proCategory = "", proName = "";
                    double proCurrentPrice = 0.0, proRawPrice = 0.0, proDiscount = 0.0;
                    int proLikesCount = 0;
                    for (String part : parts) {
                        if (part.contains("\"pro_id\":")) proId = part.split(":")[1].replaceAll("[\"}]", "");
                        else if (part.contains("\"pro_model\":")) proModel = part.split(":")[1].replaceAll("[\"}]", "");
                        else if (part.contains("\"pro_category\":")) proCategory = part.split(":")[1].replaceAll("[\"}]", "");
                        else if (part.contains("\"pro_name\":")) proName = part.split(":")[1].replaceAll("[\"}]", "");
                        else if (part.contains("\"pro_current_price\":")) proCurrentPrice = Double.parseDouble(part.split(":")[1].replaceAll("[\"}]", ""));
                        else if (part.contains("\"pro_raw_price\":")) proRawPrice = Double.parseDouble(part.split(":")[1].replaceAll("[\"}]", ""));
                        else if (part.contains("\"pro_discount\":")) proDiscount = Double.parseDouble(part.split(":")[1].replaceAll("[\"}]", ""));
                        else if (part.contains("\"pro_likes_count\":")) proLikesCount = Integer.parseInt(part.split(":")[1].replaceAll("[\"}]", ""));
                    }
                    products.add(new Product(proId, proModel, proCategory, proName, proCurrentPrice, proRawPrice, proDiscount, proLikesCount));
                }
            }
        } catch (IOException e) {
        }
        return products;
    }

    public void deleteAllProducts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_FILE))) {
            writer.write("");
        } catch (IOException e) {
        }
    }
}