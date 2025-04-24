package kr.hhplus.be.server.fixture.product

import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.product.ProductInfo
import kr.hhplus.be.server.domain.product.ProductStatistics
import kr.hhplus.be.server.domain.product.Stock
import java.time.LocalDateTime

object ProductDomainFixture {
    fun create(
        productId: Long = 1L,
        name: String = "무선 블루투스 이어폰",
        price: Int = 129000,
        description: String = "고음질 무선 블루투스 이어폰."
    ): Product {
        return Product(
            id = productId,
            name = name,
            price = price,
            description = description
        )
    }

    fun create2(
        productId: Long = 2L,
        name: String = "무선 키보드",
        price: Int = 375000,
        description: String = "적축 키보드."
    ): Product {
        return Product(
            id = productId,
            name = name,
            price = price,
            description = description
        )
    }

    fun createProducts(
        productId1: Long = 1L,
        productId2: Long = 2L
    ): List<Product> {
        return listOf(
            createProductFixture1(productId1),
            createProductFixture2(productId2)
        )
    }

    fun createProductsStatistics(
        productId1: Long = 1L,
        productId2: Long = 2L,
        productStatisticsId1: Long = 1L,
        productStatisticsId2: Long = 2L,
        productStatisticsId3: Long = 3L,
        productStatisticsId4: Long = 4L,
        productStatisticsId5: Long = 5L,
        productStatisticsId6: Long = 6L
    ): List<ProductStatistics> {
        return listOf(
            ProductStatistics(productId1, 300, LocalDateTime.now(), productStatisticsId1),
            ProductStatistics(productId1, 200, LocalDateTime.now().minusDays(1), productStatisticsId2),
            ProductStatistics(productId2, 300, LocalDateTime.now().minusDays(1), productStatisticsId3),
            ProductStatistics(productId2, 400, LocalDateTime.now().minusDays(2), productStatisticsId4),
            ProductStatistics(productId2, 100, LocalDateTime.now().minusDays(2), productStatisticsId5),
            ProductStatistics(productId2, 100, LocalDateTime.now().minusDays(3), productStatisticsId6)
        )
    }

    private fun createProductFixture1(productId: Long) = Product("무선 블루투스 이어폰", 129000, "고음질 무선 블루투스 이어폰.", productId)

    private fun createProductFixture2(productId: Long) = Product("무선 키보드", 375000, "적축 키보드.", productId)

}

object ProductStatisticsFixture {
    fun create(
        productStatisticsId: Long = 1L,
        productId: Long = 1L,
        totalSales: Int = 100,
        createdAt: LocalDateTime = LocalDateTime.now()
    ): ProductStatistics {
        return ProductStatistics(
            id = productStatisticsId,
            productId = productId,
            totalSales = totalSales,
            createdAt = createdAt
        )
    }
}

object ProductInfoFixture {
    fun create(
        productId: Long = 1L,
        name: String = "무선 블루투스 이어폰",
        price: Int = 129000,
        description: String = "고음질 무선 블루투스 이어폰. 최대 20시간 사용 가능.",
        quantity: Int = 10
    ): ProductInfo.Find {
        return ProductInfo.Find(
            productId = productId,
            name = name,
            price = price,
            description = description,
            quantity = quantity
        )
    }

    fun createProducts(
        products: List<Product> = createProducts(),
        stocks: List<Stock> = createStocks()
    ): ProductInfo.FindAll {
        return ProductInfo.FindAll(
            products = products,
            stocks = stocks
        )
    }

    private fun createProducts(): List<Product> {
        return listOf(
            createProductFixture1(),
            createProductFixture2()
        )
    }

    private fun createStocks(): List<Stock> {
        return listOf(
            createStockFixture1(),
            createStockFixture2()
        )
    }

    private fun createProductFixture1() = Product("무선 블루투스 이어폰", 129000, "고음질 무선 블루투스 이어폰.", 1L)

    private fun createProductFixture2() = Product("무선 키보드", 375000, "적축 키보드.", 2L)

    private fun createStockFixture1() = Stock(1L, 25, 1L)

    private fun createStockFixture2() = Stock(2L, 10, 2L)

}