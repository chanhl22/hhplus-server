package kr.hhplus.be.server.fixture.product

import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.product.Stock

object ProductDomainFixture {
    fun create(
        productId: Long = 1L,
        name: String = "무선 블루투스 이어폰",
        price: Int = 129000,
        description: String = "고음질 무선 블루투스 이어폰. 최대 20시간 사용 가능.",
        stockId: Long = 1L,
        quantity: Int = 25
    ): Product {
        return Product(
            id = productId,
            name = name,
            price = price,
            description = description,
            stock = Stock(
                id = stockId,
                quantity = quantity
            )
        )
    }
}