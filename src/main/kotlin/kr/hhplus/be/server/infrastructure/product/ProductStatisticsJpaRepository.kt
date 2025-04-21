package kr.hhplus.be.server.infrastructure.product

import kr.hhplus.be.server.domain.product.ProductStatistics
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ProductStatisticsJpaRepository : JpaRepository<ProductStatistics, Long> {

    fun findAllByCreatedAtBetween(start: LocalDateTime, end: LocalDateTime): List<ProductStatistics>

}