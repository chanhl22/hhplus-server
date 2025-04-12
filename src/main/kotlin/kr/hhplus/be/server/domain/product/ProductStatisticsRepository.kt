package kr.hhplus.be.server.domain.product

import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface ProductStatisticsRepository {

    fun findAllByCreatedAtBetween(start: LocalDateTime, end: LocalDateTime): List<ProductStatistics>

}