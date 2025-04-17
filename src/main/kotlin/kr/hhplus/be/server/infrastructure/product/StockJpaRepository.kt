package kr.hhplus.be.server.infrastructure.product

import kr.hhplus.be.server.domain.product.Stock
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface StockJpaRepository : JpaRepository<Stock, Long> {

    fun findByProductId(productId: Long): Optional<Stock>

    fun findByProductIdIn(productId: List<Long>): List<Stock>

}