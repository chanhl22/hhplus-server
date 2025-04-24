package kr.hhplus.be.server.application.product

import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.product.ProductCommand
import kr.hhplus.be.server.domain.product.ProductService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductFacade(
    private val productService: ProductService,
    private val orderService: OrderService
) {

    @Transactional
    fun scheduledProductStatistics() {
        val aggregateOrderProducts = orderService.aggregateOrderProduct()
        productService.saveProductStatistics(aggregateOrderProducts.map { orderProduct ->
            ProductCommand.of(productId = orderProduct.productId, totalSales = orderProduct.totalSales)
        })
    }

}