package kr.hhplus.be.server.domain.product

class Stock(
    var id: Long,
    var quantity: Int
) {
    fun isQuantityLessThan(quantity: Int): Boolean {
        return this.quantity < quantity
    }

}
