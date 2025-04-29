package kr.hhplus.be.server.infrastructure.order

import kr.hhplus.be.server.domain.order.Order
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface OrderJpaRepository : JpaRepository<Order, Long> {

    fun findByRegisteredAtBetween(yesterdayStart: LocalDateTime, yesterdayEnd: LocalDateTime): List<Order>

}