package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.math.BigDecimal.valueOf;

public class OrderCreationUseCase {
    private final OrderRepository orderRepository;
    private final ProductCatalog productCatalog;

    public OrderCreationUseCase(OrderRepository orderRepository, ProductCatalog productCatalog) {
        this.orderRepository = orderRepository;
        this.productCatalog = productCatalog;
    }

    private Product fetchProductOrThrow(String productName) {
        Product product = productCatalog.getByName(productName);
        if (product == null) {
            throw new UnknownProductException();
        }
        return product;
    }

    private OrderItem orderItemFrom(SellItemRequest itemRequest) {
        Product product = fetchProductOrThrow(itemRequest.getProductName());

        final BigDecimal taxedAmount = product.taxedAmount(itemRequest.getQuantity());
        final BigDecimal taxAmount = product.taxAmount(itemRequest.getQuantity());

        return new OrderItem(
                product,
                itemRequest.getQuantity(),
                taxedAmount,
                taxAmount
        );
    }

    private BigDecimal totalFrom(List<OrderItem> orderItems) {
        return orderItems.stream().map(OrderItem::getTaxedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal taxFrom(List<OrderItem> orderItems) {
        return orderItems.stream().map(OrderItem::getTax).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void run(SellItemsRequest request) {
        List<OrderItem> orders = request.stream()
                .map(this::orderItemFrom)
                .collect(Collectors.toList());
        Order order = new Order(
                totalFrom(orders),
                "EUR",
                orders,
                taxFrom(orders),
                OrderStatus.CREATED,
                null
        );
        orderRepository.save(order);
    }
}
