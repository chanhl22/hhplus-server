package kr.hhplus.be.server.domain.order

import org.springframework.stereotype.Service
import kr.hhplus.be.server.domain.order.OrderCommands.OrderCommand

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderProductRepository: OrderProductRepository,
    private val orderFactory: OrderFactory
) {
    fun createOrder(command: OrderCommand): Order {
        val order = orderFactory.create(command.user, command.products, command.coupon)
        orderRepository.save(order)
        orderProductRepository.saveAll(order.orderProducts)
        return order
    }

}