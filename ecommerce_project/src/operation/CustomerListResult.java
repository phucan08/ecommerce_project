package operation;

import java.util.List;
import model.Customer;

public class CustomerListResult {
    private List<Customer> list;
    private int currentPage;
    private int totalPages;

    public CustomerListResult(List<Customer> list, int currentPage, int totalPages) {
        this.list = list;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public List<Customer> getList() { return list; }
    public int getCurrentPage() { return currentPage; }
    public int getTotalPages() { return totalPages; }
}