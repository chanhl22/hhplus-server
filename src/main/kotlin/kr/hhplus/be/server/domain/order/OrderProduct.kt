package kr.hhplus.be.server.domain.order

class OrderProduct(
    val id: Long? = null,
    val order: Order,
    val productId: Long
)
