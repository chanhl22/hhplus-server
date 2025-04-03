package kr.hhplus.be.server.product.response

data class TopProductResponse(
    val productId: Long,
    val name: String,
    val price: Long,
    val soldQuantity: Int
)