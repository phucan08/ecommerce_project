package operation;

import java.util.List;
import model.Order;

public class OrderListResult {
    private List<Order> list;
    private int currentPage;
    private int totalPages;

    public OrderListResult(List<Order> list, int currentPage, int totalPages) {
        this.list = list;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public List<Order> getList() { return list; }
    public int getCurrentPage() { return currentPage; }
    public int getTotalPages() { return totalPages; }
}