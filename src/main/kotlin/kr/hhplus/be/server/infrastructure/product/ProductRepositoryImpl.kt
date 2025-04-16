package kr.hhplus.be.server.infrastructure.product

import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.product.ProductRepository
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(
    private val productJpaRepository: ProductJpaRepository
) : ProductRepository {

    override fun find(productId: Long): Product {
        return productJpaRepository.findById(productId)
            .orElseThrow(::IllegalArgumentException)
    }

    override fun findAllByIdIn(productIds: List<Long>): List<Product> {
        return productJpaRepository.findAllByIdIn(productIds)
    }

}