package kr.hhplus.be.server.fixture.product

import kr.hhplus.be.server.domain.product.ProductCommand
import kr.hhplus.be.server.domain.product.ProductCommand.OrderProduct
import kr.hhplus.be.server.domain.product.ProductCommand.OrderProducts

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