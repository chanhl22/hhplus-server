package kr.hhplus.be.server.fixture.product

import kr.hhplus.be.server.domain.product.ProductCommands.ProductCommand
import kr.hhplus.be.server.domain.product.ProductCommands.ProductsCommand

object ProductCommandFixture {
    fun createProducts(): ProductsCommand {
        return ProductsCommand.of(
            listOf(
                ProductCommand(1L, 25),
                ProductCommand(2L, 10)
            )
        )
    }

}