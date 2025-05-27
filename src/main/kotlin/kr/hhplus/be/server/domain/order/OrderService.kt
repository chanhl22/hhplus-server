package kr.hhplus.be.server.domain.order

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderProductRepository: OrderProductRepository,
    private val orderEventPublisher: OrderEventPublisher
) {

    fun aggregateOrderProduct(): List<OrderInfo.ProductStatistics> {
        val yesterdayStart = LocalDate.now().minusDays(1).atStartOfDay()
        val yesterdayEnd = LocalDate.now().atStartOfDay().minusNanos(1)

        val orders: List<Order> = orderRepository.findByRegisteredAtBetween(yesterdayStart, yesterdayEnd)
        val orderProducts: List<OrderProduct> = orderProductRepository.findByOrderIn(orders)

        val productCountMap: Map<Long, Int> = orderProducts
            .groupingBy { it.productId }
            .eachCount()

        return productCountMap
            .map { (productId, totalSales) -> OrderInfo.of(productId, totalSales) }
    }

    @Transactional
    fun ready(command: OrderCommand.Create): Long {
        val order = Order.ready(command.userId, command.getProductPairs(), command.couponId)

        val savedOrder = orderRepository.save(order)
        orderProductRepository.saveAll(order.orderProducts)

        orderEventPublisher.publish(command.toEvent(savedOrder.id))

        return savedOrder.id
    }

    @Transactional
    fun complete(command: OrderCommand.Completed) {
        val order = orderRepository.find(command.orderId)
        val successOrder =
            order.completed(command.userId, command.getProductPairs(), command.couponId, command.totalPrice)

        orderRepository.save(successOrder)
        orderProductRepository.saveAll(order.orderProducts)

        orderEventPublisher.publish(command.toEvent())
    }

}