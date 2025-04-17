package kr.hhplus.be.server.infrastructure.product

import kr.hhplus.be.server.domain.product.Stock
import kr.hhplus.be.server.domain.product.StockRepository
import org.springframework.stereotype.Repository

@Repository
class StockRepositoryImpl(
    private val stockJpaRepository: StockJpaRepository
) : StockRepository {

    override fun findByProductId(productId: Long): Stock {
        return stockJpaRepository.findByProductId(productId)
            .orElseThrow(::IllegalArgumentException)
    }

    override fun findByProductIdIn(productIds: List<Long>): List<Stock> {
        return stockJpaRepository.findByProductIdIn(productIds)
    }

    override fun saveAll(stocks: List<Stock>) {
        stockJpaRepository.saveAll(stocks)
    }

}