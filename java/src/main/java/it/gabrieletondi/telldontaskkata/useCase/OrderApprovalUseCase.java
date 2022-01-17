package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;

public class OrderApprovalUseCase {
    private final OrderRepository orderRepository;

    public OrderApprovalUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void run(OrderApprovalRequest request) {
        Order savedOrder = orderRepository.getById(request.getOrderId());

        Order updatedOrder = updateOrderOrThrow(request, savedOrder);

        orderRepository.save(updatedOrder);
    }

    private Order updateOrderOrThrow(OrderApprovalRequest request, Order order) {
        if (order.isShipped()) {
            throw new ShippedOrdersCannotBeChangedException();
        }

        if (request.isApproved() && order.isRejected()) {
            throw new RejectedOrderCannotBeApprovedException();
        }

        if (!request.isApproved() && order.isApproved()) {
            throw new ApprovedOrderCannotBeRejectedException();
        }

        return Order.updateFrom(order, request);
    }
}
