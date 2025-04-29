package kr.hhplus.be.server.infrastructure.product

import jakarta.persistence.LockModeType
import kr.hhplus.be.server.domain.product.Stock
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface StockJpaRepository : JpaRepository<Stock, Long> {

    fun findByProductId(productId: Long): Optional<Stock>

    fun findByProductIdIn(productId: List<Long>): List<Stock>

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select s from Stock s where s.productId in :productIds")
    fun findByProductIdInWithOptimisticLock(productIds: List<Long>): List<Stock>

}