package kr.hhplus.be.server.domain.order.coupon

import kr.hhplus.be.server.domain.coupon.DiscountType
import kr.hhplus.be.server.fixture.coupon.CouponDomainFixture
import kr.hhplus.be.server.fixture.coupon.RealOrderCouponFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class RealOrderCouponTest {

    @DisplayName("총 주문 금액에 금액 할인 쿠폰을 적용한다.")
    @Test
    fun applyAmount() {
        //given
        val realOrderCoupon = RealOrderCouponFixture.create(
            discountType = DiscountType.AMOUNT,
            discountValue = 1000,
        )

        //when
        val result = realOrderCoupon.apply(10000)

        // then
        assertThat(result).isEqualTo(9000)
    }

    @DisplayName("총 주문 금액에 퍼센트 할인 쿠폰을 적용한다.")
    @Test
    fun applyPercent() {
        //given
        val realOrderCoupon = RealOrderCouponFixture.create(
            discountType = DiscountType.PERCENT,
            discountValue = 10,
        )

        //when
        val result = realOrderCoupon.apply(10000)

        // then
        assertThat(result).isEqualTo(9000)
    }

    @DisplayName("쿠폰의 id를 조회한다.")
    @Test
    fun getCouponId() {
        //given
        val realOrderCoupon = RealOrderCouponFixture.create(couponId = 1L)

        //when
        val result = realOrderCoupon.getCouponId()

        // then
        assertThat(result).isEqualTo(1L)
    }

    @DisplayName("쿠폰을 적용했다면 진짜 쿠폰을 생성한다.")
    @Test
    fun create() {
        //given
        val coupon = CouponDomainFixture.create(
            name = "1000원 할인 쿠폰",
            discountType = DiscountType.AMOUNT,
            discountValue = 1000,
        )

        //when
        val realOrderCoupon = RealOrderCoupon.create(coupon)

        // then
        assertThat(realOrderCoupon)
            .extracting("name", "discountType", "discountValue")
            .containsExactly("1000원 할인 쿠폰", DiscountType.AMOUNT, 1000)
    }

}