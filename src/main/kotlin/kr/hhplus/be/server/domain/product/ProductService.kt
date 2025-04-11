package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.domain.product.ProductCommands.ProductsCommand
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository
) {
    fun find(productId: Long): Product {
        return productRepository.find(productId)
    }

    fun findAll(command: ProductsCommand): List<Product> {
        val products = productRepository.findAllWithStockByIdIn(command.getProductIds())

        val productMap: Map<Long, Product> = products.associateBy { it.id }
        return command.products.map { commandProduct ->
            val product = productMap[commandProduct.productId]
                ?: throw IllegalArgumentException("존재하지 않는 상품입니다. id: ${commandProduct.productId}")
            product.validateStockEnough(commandProduct.quantity)
            product
        }
    }

}