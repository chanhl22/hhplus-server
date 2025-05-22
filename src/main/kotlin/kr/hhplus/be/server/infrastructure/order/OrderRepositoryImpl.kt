package kr.hhplus.be.server.infrastructure.order

import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.order.OrderRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class OrderRepositoryImpl(
    private val orderJpaRepository: OrderJpaRepository
) : OrderRepository {

    override fun save(order: Order): Order {
        return orderJpaRepository.save(order)
    }

    override fun findByRegisteredAtBetween(yesterdayStart: LocalDateTime, yesterdayEnd: LocalDateTime): List<Order> {
        return orderJpaRepository.findByRegisteredAtBetween(yesterdayStart, yesterdayEnd)
    }

    override fun find(orderId: Long): Order {
        return orderJpaRepository.findById(orderId)
            .orElseThrow(::IllegalArgumentException)
    }

}