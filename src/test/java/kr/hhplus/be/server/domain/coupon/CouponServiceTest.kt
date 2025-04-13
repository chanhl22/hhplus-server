package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.coupon.CouponCommands.IssueCouponCommand
import kr.hhplus.be.server.fixture.coupon.CouponDomainFixture
import kr.hhplus.be.server.fixture.user.UserFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any

@ExtendWith(MockitoExtension::class)
class CouponServiceTest {

    @Mock
    private lateinit var couponRepository: CouponRepository

    @Mock
    private lateinit var userCouponRepository: UserCouponRepository

    @InjectMocks
    private lateinit var couponService: CouponService

    @DisplayName("주문을 생성한다.")
    @Test
    fun createOrder() {
        //given
        val user = UserFixture.create()
        val coupon = CouponDomainFixture.create(user = user)
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

    @DisplayName("쿠폰을 발행한다.")
    @Test
    fun issueCoupon() {
        //given
        val coupon = CouponDomainFixture.create()
        BDDMockito.given(couponRepository.find(anyLong()))
            .willReturn(coupon)

        BDDMockito.given(couponRepository.save(any()))
            .willReturn(coupon)

        val userCoupon = CouponDomainFixture.createUserCoupon()
        BDDMockito.given(userCouponRepository.save(any()))
            .willReturn(userCoupon)

        //when
        val command = IssueCouponCommand.of(UserFixture.create(), coupon.id)
        couponService.issueCoupon(command)

        //then
        Mockito.verify(couponRepository, times(1))
            .find(anyLong())
        Mockito.verify(couponRepository, times(1))
            .save(any())
        Mockito.verify(userCouponRepository, times(1))
            .save(any())
    }

}
