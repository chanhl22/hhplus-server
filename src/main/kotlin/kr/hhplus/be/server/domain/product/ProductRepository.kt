package kr.hhplus.be.server.domain.product

interface ProductRepository {

    fun find(productId: Long): Product

    fun findAllByIdIn(productIds: List<Long>): List<Product>

}