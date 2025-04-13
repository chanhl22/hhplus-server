package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.coupon.DiscountType
import kr.hhplus.be.server.fixture.product.ProductDomainFixture
import kr.hhplus.be.server.fixture.user.UserFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class OrderTest {

    @DisplayName("주문을 생성한다.")
    @Test
    fun create1() {
        //given
        val user = UserFixture.create()
        val products = ProductDomainFixture.createProducts()

        //when
        val result = Order.create(user, products)

        //then
        assertThat(result.totalPrice).isEqualTo(504000)
    }

    @DisplayName("쿠폰을 적용한 주문을 생성한다.")
    @Test
    fun create2() {
        //given
        val user = UserFixture.create()
        val products = ProductDomainFixture.createProducts()
        val coupon = Coupon(1L, user, DiscountType.AMOUNT, 1000, LocalDateTime.now().plusMonths(1))

        //when
        val result = Order.create(user, products, coupon)

        //then
        assertThat(result.totalPrice).isEqualTo(503000)
    }

}