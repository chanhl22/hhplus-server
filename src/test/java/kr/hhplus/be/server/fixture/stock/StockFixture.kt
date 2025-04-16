package kr.hhplus.be.server.fixture.stock

import kr.hhplus.be.server.domain.product.Stock

object StockDomainFixture {
    fun create(
        stockId: Long = 1L,
        productId: Long = 1L,
        quantity: Int = 10000
    ): Stock {
        return Stock(
            id = stockId,
            productId = productId,
            quantity = quantity
        )
    }

    fun createStocks(
        stockId1: Long = 1L,
        stockId2: Long = 2L,
    ): List<Stock> {
        return listOf(
            createStockFixture1(stockId1),
            createStockFixture2(stockId2)
        )
    }

    private fun createStockFixture1(stockId: Long) = Stock(1L, 25, stockId)

    private fun createStockFixture2(stockId: Long) = Stock(2L, 10, stockId)
}