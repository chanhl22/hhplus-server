package kr.hhplus.be.server.domain.order

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate


@Service
@Transactional(readOnly = true)
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderProductRepository: OrderProductRepository,
) {
    @Transactional
    fun order(command: OrderCommand.Order): Order {
        val order = Order.create(command.orderPoint, command.orderedProducts, command.orderCoupon)

        val savedOrder = orderRepository.save(order)
        orderProductRepository.saveAll(order.orderProducts)
        return savedOrder
    }

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

}