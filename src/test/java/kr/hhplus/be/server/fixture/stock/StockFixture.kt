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
        quantity1: Int = 25,
        quantity2: Int = 10,
        productId1: Long = 1L,
        productId2: Long = 1L,
    ): List<Stock> {
        return listOf(
            createStockFixture1(stockId1, quantity1, productId1),
            createStockFixture2(stockId2, quantity2, productId2)
        )
    }

    private fun createStockFixture1(stockId: Long, quantity1: Int, productId1: Long) = Stock(productId1, quantity1, stockId)

    private fun createStockFixture2(stockId: Long, quantity2: Int, productId2: Long) = Stock(productId2, quantity2, stockId)
}