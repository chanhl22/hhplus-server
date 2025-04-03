package kr.hhplus.be.server.order.response

data class OrderResponse(
    val orderId: Long,
    val totalAmount: Long,
    val finalBalance: Long,
    val status: String
)
