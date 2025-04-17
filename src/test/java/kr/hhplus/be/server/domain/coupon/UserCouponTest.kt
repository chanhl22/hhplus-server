package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.fixture.coupon.UserCouponDomainFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class UserCouponTest {

    @DisplayName("사용자의 쿠폰을 UserCoupone에 등록한다.")
    @Test
    fun create() {
        //when
        val userCoupon = UserCoupon.create(1L, 1L)

        // then
        assertThat(userCoupon.isUsed).isEqualTo(false)
    }

    @DisplayName("쿠폰을 사용했다고 변경한다.")
    @Test
    fun used() {
        //given
        val userCoupon = UserCouponDomainFixture.create()

        //when
        val used = userCoupon.used()

        // then
        assertThat(used.isUsed).isEqualTo(true)
    }

}