package kr.hhplus.be.server.domain.product

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "stock")
class Stock(

    val productId: Long,

    val quantity: Int,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
) {
    fun validateQuantity() {
        if (quantity < 0) {
            throw IllegalArgumentException("상품 재고는 0보다 작을 수 없습니다.")
        }
    }

    fun deduct(orderQuantity: Int): Stock {
        if (isSoldOut(orderQuantity)) {
            throw IllegalStateException("재고가 모두 소진되었습니다.")
        }
        val deductQuantity = quantity - orderQuantity
        return Stock(productId, deductQuantity, id)
    }

    private fun isSoldOut(orderQuantity: Int): Boolean {
        return this.quantity < orderQuantity
    }

}
