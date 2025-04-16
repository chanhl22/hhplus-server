package kr.hhplus.be.server.fixture.product

import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.product.ProductCommand
import kr.hhplus.be.server.domain.product.ProductCommand.OrderProduct
import kr.hhplus.be.server.domain.product.ProductCommand.OrderProducts
import kr.hhplus.be.server.domain.product.ProductInfo
import kr.hhplus.be.server.domain.product.ProductStatistics
import java.time.LocalDateTime

object ProductCommandFixture {
    fun createProducts(): OrderProducts {
        return ProductCommand.of(
            listOf(
                OrderProduct(1L, 25),
                OrderProduct(2L, 10)
            )
        )
    }

}

object ProductDomainFixture {
    fun create(
        productId: Long = 1L,
        name: String = "무선 블루투스 이어폰",
        price: Int = 129000,
        description: String = "고음질 무선 블루투스 이어폰. 최대 20시간 사용 가능."
    ): Product {
        return Product(
            id = productId,
            name = name,
            price = price,
            description = description
        )
    }

    fun createProducts(): List<Product> {
        return listOf(
            createProductFixture1(),
            createProductFixture2()
        )
    }

    fun createProductsStatistics(): List<ProductStatistics> {
        return listOf(
            ProductStatistics(1L, createProductFixture1(), 350, LocalDateTime.now()),
            ProductStatistics(2L, createProductFixture2(), 280, LocalDateTime.now())
        )
    }

    private fun createProductFixture1() = Product(1L, "무선 블루투스 이어폰", 129000, "고음질 무선 블루투스 이어폰.")

    private fun createProductFixture2() = Product(2L, "무선 키보드", 375000, "적축 키보드.")

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
}