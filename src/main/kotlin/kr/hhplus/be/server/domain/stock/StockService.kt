package kr.hhplus.be.server.domain.stock

import kr.hhplus.be.server.domain.product.Stock
import org.springframework.stereotype.Service

@Service
class StockService(
    private val stockRepository: StockRepository
) {
    fun findAll(productIds: List<Long>): List<Stock> {
        return stockRepository.findByProductIdIn(productIds)
    }

}

