package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.fixture.coupon.CouponDomainFixture
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
import kr.hhplus.be.server.domain.coupon.CouponCommands.IssueCouponCommand

@ExtendWith(MockitoExtension::class)
class CouponServiceTest {

    @Mock
    private lateinit var couponRepository: CouponRepository

    @Mock
    private lateinit var userCouponRepository: UserCouponRepository

    @Mock
    private lateinit var couponFactory: CouponFactory

    @DisplayName("주문을 생성한다.")
    @Test
    fun createOrder() {
        //given
        val couponService = CouponService(couponRepository, userCouponRepository, couponFactory)

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

    @DisplayName("쿠폰을 발급한다.")
    @Test
    fun issueCoupon() {
        //given
        val couponService = CouponService(couponRepository, userCouponRepository, couponFactory)

        val user = UserFixture.create()
        val coupon = CouponDomainFixture.create(user = user)
        BDDMockito.given(couponRepository.find(coupon.id))
            .willReturn(coupon)

        BDDMockito.given(couponRepository.save(coupon))
            .willReturn(coupon)

        val userCoupon = UserCoupon(1L, user, coupon)
        BDDMockito.given(couponFactory.create(user, coupon))
            .willReturn(userCoupon)
        BDDMockito.given(userCouponRepository.save(userCoupon))
            .willReturn(userCoupon)


        //when
        val command = IssueCouponCommand.of(user, coupon.id)
        val result = couponService.issueCoupon(command)

        //then
        assertThat(result)
            .extracting("name", "discountType")
            .containsExactly(coupon.name, coupon.discountType)
        Mockito.verify(couponRepository, times(1))
            .find(coupon.id)
        Mockito.verify(couponRepository, times(1))
            .save(coupon)
        Mockito.verify(userCouponRepository, times(1))
            .save(userCoupon)
    }

}
