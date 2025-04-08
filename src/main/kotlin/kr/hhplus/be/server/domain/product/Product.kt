package kr.hhplus.be.server.domain.product

class Product(
    val id: Long,
    val name: String,
    val price: Int,
    val description: String,
    val stock: Stock
)
