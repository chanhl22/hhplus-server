package kr.hhplus.be.server.order.request

data class OrderProductRequest(
    val productId: Long,
    val quantity: Int
)
