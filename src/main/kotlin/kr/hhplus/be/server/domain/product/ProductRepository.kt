package kr.hhplus.be.server.domain.product

import org.springframework.stereotype.Repository

@Repository
interface ProductRepository {

    fun find(productId: Long): Product

    fun findAllByIdIn(productIds: List<Long>): List<Product>

}