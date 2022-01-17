package it.gabrieletondi.telldontaskkata.domain;

import it.gabrieletondi.telldontaskkata.useCase.OrderApprovalRequest;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    private BigDecimal total;
    private String currency;
    private List<OrderItem> items;
    private BigDecimal tax;
    private OrderStatus status;
    private int id;

    public Order() {}

    public Order(BigDecimal total, String currency, List<OrderItem> items, BigDecimal tax, OrderStatus status, int id) {
        this.total = total;
        this.currency = currency;
        this.items = items;
        this.tax = tax;
        this.status = status;
        this.id = id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private boolean statusEquals(OrderStatus shipped) {
        return status.equals(shipped);
    }

    public boolean isShipped() {
        return statusEquals(OrderStatus.SHIPPED);
    }

    public boolean isRejected() {
        return statusEquals(OrderStatus.REJECTED);
    }

    public boolean isApproved() {
        return statusEquals(OrderStatus.APPROVED);
    }

    public boolean isCreated() {
        return statusEquals(OrderStatus.CREATED);
    }

    public static Order updateFrom(Order order, OrderApprovalRequest request) {
        return new Order(
                order.total,
                order.currency,
                order.items,
                order.tax,
                request.isApproved() ? OrderStatus.APPROVED : OrderStatus.REJECTED,
                order.id
        );
    }

}
