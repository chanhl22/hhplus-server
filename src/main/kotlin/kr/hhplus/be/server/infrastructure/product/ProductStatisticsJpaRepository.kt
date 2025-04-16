package kr.hhplus.be.server.infrastructure.product

import kr.hhplus.be.server.domain.product.ProductStatistics
import org.springframework.data.jpa.repository.JpaRepository

interface ProductStatisticsJpaRepository : JpaRepository<ProductStatistics, Long>