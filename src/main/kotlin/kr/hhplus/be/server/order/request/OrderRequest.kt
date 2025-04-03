package kr.hhplus.be.server.order.request

data class OrderRequest(
    val userId: Long,
    val items: List<OrderProductRequest>
)
