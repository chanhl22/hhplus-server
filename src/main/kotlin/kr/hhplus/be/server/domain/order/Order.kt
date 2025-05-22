package kr.hhplus.be.server.domain.order

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class Order(

    val userId: Long,

    val totalPrice: Int,

    val couponId: Long?,

    val registeredAt: LocalDateTime = LocalDateTime.now(),

    @Enumerated(EnumType.STRING)
    val status: OrderStatus,

    @OneToMany(mappedBy = "order")
    val orderProducts: MutableList<OrderProduct> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) {
    companion object {
        fun ready(userId: Long, productPairs: List<Pair<Long, Int>>, couponId: Long?): Order {
            val order = Order(
                userId = userId,
                totalPrice = 0,
                couponId = couponId,
                status = OrderStatus.PENDING
            )

            order.initOrderProducts(productPairs)
            return order
        }
    }

    fun completed(userId: Long, productPairs: List<Pair<Long, Int>>, couponId: Long?, totalPrice: Int): Order {
        val order = Order(
            userId = userId,
            totalPrice = totalPrice,
            couponId = couponId,
            status = OrderStatus.COMPLETED
        )

        order.initOrderProducts(productPairs)
        return order
    }

    private fun initOrderProducts(productIds: List<Pair<Long, Int>>) {
        this.orderProducts.addAll(
            productIds.map { pair ->
                OrderProduct(order = this, productId = pair.first, quantity = pair.second)
            }
        )
    }

}

enum class OrderStatus(
    val description: String
) {
    CREATED("생성"),
    PENDING("대기"),
    COMPLETED("완료"),
    CANCELED("취소")
}