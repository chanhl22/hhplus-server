package kr.hhplus.be.server.domain.product

interface StockRepository {

    fun findByProductId(productId: Long): Stock

    fun findByProductIdIn(productIds: List<Long>): List<Stock>

    fun saveAll(stocks: List<Stock>)

}