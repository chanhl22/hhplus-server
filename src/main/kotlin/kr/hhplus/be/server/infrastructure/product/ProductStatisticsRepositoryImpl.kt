package kr.hhplus.be.server.infrastructure.product

import kr.hhplus.be.server.domain.product.ProductStatistics
import kr.hhplus.be.server.domain.product.ProductStatisticsRepository
import kr.hhplus.be.server.domain.product.ProductWithStatDto
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ProductStatisticsRepositoryImpl(
    private val productStatisticsJpaRepository: ProductStatisticsJpaRepository
) : ProductStatisticsRepository {
    override fun findAllByCreatedAtBetween(start: LocalDateTime, end: LocalDateTime): List<ProductStatistics> {
        return productStatisticsJpaRepository.findAllByCreatedAtBetween(start, end)
    }

    override fun findStatsWithProductInfo(start: LocalDateTime, end: LocalDateTime): List<ProductWithStatDto> {
        return productStatisticsJpaRepository.findStatsWithProductInfo(start, end)
    }


}