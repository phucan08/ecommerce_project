package operation;

import java.util.List;
import model.Product;

public class ProductListResult {
    private List<Product> list;
    private int currentPage;
    private int totalPages;

    public ProductListResult(List<Product> list, int currentPage, int totalPages) {
        this.list = list;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public List<Product> getList() { return list; }
    public int getCurrentPage() { return currentPage; }
    public int getTotalPages() { return totalPages; }
}