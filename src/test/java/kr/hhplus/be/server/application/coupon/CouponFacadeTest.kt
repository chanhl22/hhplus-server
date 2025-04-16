package kr.hhplus.be.server.application.coupon

import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.user.UserService
import kr.hhplus.be.server.fixture.coupon.CouponCriterionFixture
import kr.hhplus.be.server.fixture.coupon.CouponDomainFixture
import kr.hhplus.be.server.fixture.user.UserDomainFixture
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
class CouponFacadeTest {

    @Mock
    private lateinit var couponService: CouponService

    @Mock
    private lateinit var userService: UserService

    @InjectMocks
    private lateinit var orderFacade: CouponFacade

    @DisplayName("선착순 쿠폰을 발급한다.")
    @Test
    fun issueCouponFirstCome() {
        //given
        val user = UserDomainFixture.create()
        BDDMockito.given(userService.find(anyLong()))
            .willReturn(user)

        val coupon = CouponDomainFixture.create()
        BDDMockito.given(couponService.issueCoupon(any()))
            .willReturn(coupon)

        //when
        val couponCriterion = CouponCriterionFixture.create()
        orderFacade.issueCouponFirstCome(couponCriterion)

        //then
        Mockito.verify(userService, times(1))
            .find(anyLong())
        Mockito.verify(couponService, times(1))
            .issueCoupon(any())
    }

}