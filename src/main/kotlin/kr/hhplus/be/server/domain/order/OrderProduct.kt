package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.product.Product

class OrderProduct(
    val id: Long = 0L,
    val order: Order,
    val product: Product
) {

}
