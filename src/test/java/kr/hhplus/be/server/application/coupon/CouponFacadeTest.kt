package kr.hhplus.be.server.application.coupon

import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.point.Point
import kr.hhplus.be.server.domain.user.User
import kr.hhplus.be.server.domain.user.UserService
import kr.hhplus.be.server.fixture.coupon.CouponDomainFixture
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class CouponFacadeTest {

    @Mock
    private lateinit var couponService: CouponService

    @Mock
    private lateinit var userService: UserService

    @DisplayName("선착순 쿠폰을 발급한다.")
    @Test
    fun issueCouponFirstCome() {
        //given
        val orderFacade = CouponFacade(couponService, userService)

        val user = User(1L, "이찬희B", Point(1L, 100000))
        BDDMockito.given(userService.find(ArgumentMatchers.anyLong()))
            .willReturn(user)

        val coupon = CouponDomainFixture.create(user = user)
        val couponCriterion = CouponCriteria.CouponCriterion.of(user.id, coupon.id)
        BDDMockito.given(couponService.issueCoupon(couponCriterion.toCommand(user)))
            .willReturn(coupon)

        //when
        orderFacade.issueCouponFirstCome(couponCriterion)

        //then
        Mockito.verify(userService, times(1))
            .find(anyLong())
        Mockito.verify(couponService, times(1))
            .issueCoupon(couponCriterion.toCommand(user))
    }

}