package kr.hhplus.be.server.domain.stock

import kr.hhplus.be.server.domain.product.Stock
import org.springframework.stereotype.Repository

@Repository
interface StockRepository {

    fun findByProductId(productId: Long): Stock

    fun findByProductIdIn(productIds: List<Long>): List<Stock>

}