package kr.hhplus.be.server.product.response

data class TopSellingProductResponse(
    val rank: Int,
    val productId: Long,
    val name: String,
    val price: Long,
    val soldQuantity: Int,
    val stock: Int,
)