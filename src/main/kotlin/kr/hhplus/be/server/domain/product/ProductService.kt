package kr.hhplus.be.server.domain.product

import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository
) {
    fun find(productId: Long): Product {
        return productRepository.find(productId)
    }
}