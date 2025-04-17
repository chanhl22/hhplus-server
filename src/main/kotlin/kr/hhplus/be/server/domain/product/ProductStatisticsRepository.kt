package kr.hhplus.be.server.domain.product

import java.time.LocalDateTime

interface ProductStatisticsRepository {

    fun findAllByCreatedAtBetween(start: LocalDateTime, end: LocalDateTime): List<ProductStatistics>

}