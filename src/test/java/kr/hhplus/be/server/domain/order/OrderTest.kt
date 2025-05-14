package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.fixture.order.OrderCouponFixture
import kr.hhplus.be.server.fixture.order.OrderPointFixture
import kr.hhplus.be.server.fixture.order.OrderedProductsFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class OrderTest {

    @DisplayName("주문을 생성한다.")
    @Test
    fun create() {
        //given
        val orderPoint = OrderPointFixture.create()
        val orderedProducts = OrderedProductsFixture.create(
            listOf(
                OrderedProduct(1L, "상품1", 10000, 10),
                OrderedProduct(2L, "상품2", 20000, 20)
            ),
            mapOf(
                1L to 2,
                2L to 1
            )
        )
        val orderCoupon = OrderCouponFixture.create()

        //when
        val result = Order.create(orderPoint, orderedProducts, orderCoupon)

        //then
        assertThat(result.totalPrice).isEqualTo(40000)
    }

}