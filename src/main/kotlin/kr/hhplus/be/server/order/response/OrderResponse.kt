package kr.hhplus.be.server.order.response

data class OrderResponse(
    val orderId: Long,
    val finalAmount: Long,
    val status: String
)
