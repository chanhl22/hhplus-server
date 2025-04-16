package kr.hhplus.be.server.domain.order

import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderProductRepository: OrderProductRepository,
) {
    fun order(command: OrderCommand.Order): Order {
        val order = Order.create(command.orderPoint, command.orderedProducts, command.orderCoupon)

        val savedOrder = orderRepository.save(order)
        orderProductRepository.saveAll(order.orderProducts)
        return savedOrder
    }

}