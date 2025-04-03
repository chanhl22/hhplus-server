package kr.hhplus.be.server.product.response

data class ProductResponse(
    val productId: Long,
    val name: String,
    val price: Long,
    val stock: Int,
    val description: String
)