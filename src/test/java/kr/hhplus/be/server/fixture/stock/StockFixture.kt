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

    fun createStocks(): List<Stock> {
        return listOf(
            createStockFixture1(),
            createStockFixture2()
        )
    }

    private fun createStockFixture1() = Stock(1L, 1L, 25)

    private fun createStockFixture2() = Stock(2L, 2L, 10)
}