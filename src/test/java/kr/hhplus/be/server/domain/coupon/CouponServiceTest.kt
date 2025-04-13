package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.fixture.coupon.CouponCommandsFixture
import kr.hhplus.be.server.fixture.coupon.CouponDomainFixture
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

    @DisplayName("쿠폰을 조회한다.")
    @Test
    fun find() {
        //given
        val userCoupon = CouponDomainFixture.createUserCoupon()
        BDDMockito.given(userCouponRepository.findByCouponIdAndUserId(anyLong(), anyLong()))
            .willReturn(listOf(userCoupon))

        //when
        couponService.find(1L, 1L)

        //then
        Mockito.verify(userCouponRepository, times(1))
            .findByCouponIdAndUserId(anyLong(), anyLong())
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
        val command = CouponCommandsFixture.createIssueCouponCommand()
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
