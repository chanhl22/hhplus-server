package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.fixture.user.UserFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class CouponServiceTest {

    @Mock
    private lateinit var couponRepository: CouponRepository

    @Mock
    private lateinit var userCouponRepository: UserCouponRepository

    @DisplayName("주문을 생성한다.")
    @Test
    fun createOrder() {
        //given
        val couponService = CouponService(couponRepository, userCouponRepository)

        val user = UserFixture.create()
        val coupon = Coupon(1L, user, DiscountType.AMOUNT, 1000, LocalDateTime.now().plusMonths(1))
        val userCoupon = UserCoupon(1L, user, coupon)
        BDDMockito.given(userCouponRepository.findByCouponIdAndUserId(coupon.id, user.id))
            .willReturn(listOf(userCoupon))

        //when
        val result = couponService.find(coupon.id, user.id)

        //then
        assertThat(result)
            .extracting("id", "discountType")
            .containsExactly(coupon.id, coupon.discountType)
        Mockito.verify(userCouponRepository, times(1))
            .findByCouponIdAndUserId(coupon.id, user.id)
    }

}
