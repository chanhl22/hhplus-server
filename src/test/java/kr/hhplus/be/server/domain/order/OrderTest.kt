package kr.hhplus.be.server.domain.order

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class OrderTest {

    @Test
    @DisplayName("주문을 대기 상태로 생성한다.")
    fun createReadyOrder() {
        // given
        val userId = 1L
        val couponId = 10L
        val productPairs = listOf(1L to 2, 2L to 3) // productId to quantity

        // when
        val order = Order.ready(userId, productPairs, couponId)

        // then
        assertThat(order.userId).isEqualTo(userId)
        assertThat(order.couponId).isEqualTo(couponId)
        assertThat(order.status).isEqualTo(OrderStatus.PENDING)
        assertThat(order.totalPrice).isEqualTo(0)
        assertThat(order.orderProducts).hasSize(2)

        val firstProduct = order.orderProducts[0]
        assertThat(firstProduct.productId).isEqualTo(1L)
        assertThat(firstProduct.quantity).isEqualTo(2)

        val secondProduct = order.orderProducts[1]
        assertThat(secondProduct.productId).isEqualTo(2L)
        assertThat(secondProduct.quantity).isEqualTo(3)
    }

    @Test
    @DisplayName("주문을 완료 상태로 생성한다.")
    fun createCompletedOrder() {
        // given
        val userId = 2L
        val couponId = null
        val totalPrice = 5000
        val productPairs = listOf(3L to 1)

        // when
        val order = Order(
            userId = userId,
            totalPrice = totalPrice,
            couponId = couponId,
            status = OrderStatus.COMPLETED
        )
        order.orderProducts.addAll(
            productPairs.map { OrderProduct(order = order, productId = it.first, quantity = it.second) }
        )

        // then
        assertThat(order.userId).isEqualTo(userId)
        assertThat(order.status).isEqualTo(OrderStatus.COMPLETED)
        assertThat(order.totalPrice).isEqualTo(totalPrice)
        assertThat(order.orderProducts).hasSize(1)
        assertThat(order.orderProducts[0].productId).isEqualTo(3L)
        assertThat(order.orderProducts[0].quantity).isEqualTo(1)
    }

}