package kr.hhplus.be.server.domain.order

import java.time.LocalDateTime

interface OrderRepository {

    fun save(order: Order): Order

    fun findByRegisteredAtBetween(yesterdayStart: LocalDateTime, yesterdayEnd: LocalDateTime): List<Order>

}