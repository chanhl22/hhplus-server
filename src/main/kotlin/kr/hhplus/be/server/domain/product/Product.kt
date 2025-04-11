package kr.hhplus.be.server.domain.product

class Product(
    val id: Long,
    val name: String,
    val price: Int,
    val description: String,
    val stock: Stock
) {
    fun validateStockEnough(orderQuantity: Int) {
        if (this.stock.isQuantityLessThan(orderQuantity)) {
            throw IllegalArgumentException("재고가 부족한 상품이 있습니다. id: $id")
        }
    }
}