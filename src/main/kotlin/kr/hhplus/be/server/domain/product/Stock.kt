package kr.hhplus.be.server.domain.product

class Stock(
    val id: Long,
    val productId: Long,
    var quantity: Int
) {
    fun validateQuantity() {
        if (quantity < 0) {
            throw IllegalArgumentException("상품 재고는 0보다 작을 수 없습니다.")
        }
    }

}
