package kr.hhplus.be.server.product.response

data class ProductResponse(
    val id: Long,
    val name: String,
    val price: Long,
    val stock: Int
)