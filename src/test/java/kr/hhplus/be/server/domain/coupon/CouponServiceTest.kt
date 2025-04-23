package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.fixture.coupon.CouponDomainFixture
import kr.hhplus.be.server.fixture.coupon.UserCouponDomainFixture
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.never

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
        BDDMockito.given(userCouponRepository.existsByCouponIdAndUserIdAndIsUsed(any(), any(), any()))
            .willReturn(true)

        val coupon = CouponDomainFixture.create()
        BDDMockito.given(couponRepository.find(any()))
            .willReturn(coupon)

        //when
        couponService.find(1L, 1L)

        //then
        Mockito.verify(userCouponRepository, times(1))
            .existsByCouponIdAndUserIdAndIsUsed(any(), any(), any())
        Mockito.verify(couponRepository, times(1))
            .find(any())
    }

    @DisplayName("쿠폰이 존재하지 않으면 조회하지 않는다.")
    @Test
    fun emptyCoupon() {
        //given
        BDDMockito.given(userCouponRepository.existsByCouponIdAndUserIdAndIsUsed(any(), any(), any()))
            .willReturn(false)

        //when
        couponService.find(1L, 1L)

        //then
        Mockito.verify(userCouponRepository, times(1))
            .existsByCouponIdAndUserIdAndIsUsed(any(), any(), any())
        Mockito.verify(couponRepository, never())
            .find(any())
    }

    @DisplayName("쿠폰을 발행한다.")
    @Test
    fun issueCoupon() {
        //given
        val coupon = CouponDomainFixture.create()
        BDDMockito.given(couponRepository.findWithPessimisticLock(any()))
            .willReturn(coupon)

        BDDMockito.given(couponRepository.save(any()))
            .willReturn(coupon)

        val userCoupon = UserCouponDomainFixture.create()
        BDDMockito.given(userCouponRepository.save(any()))
            .willReturn(userCoupon)

        //when
        couponService.issueCoupon(1L, 1L)

        //then
        Mockito.verify(couponRepository, times(1))
            .findWithPessimisticLock(any())
        Mockito.verify(couponRepository, times(1))
            .save(any())
        Mockito.verify(userCouponRepository, times(1))
            .save(any())
    }

    @DisplayName("쿠폰을 사용한다.")
    @Test
    fun isUsed() {
        //given
        val userCoupons = listOf(
            UserCouponDomainFixture.create(),
            UserCouponDomainFixture.create()
        )
        BDDMockito.given(userCouponRepository.findByCouponIdAndUserIdAndIsUsed(any(), any(), any()))
            .willReturn(userCoupons)

        val userCoupon = UserCouponDomainFixture.create()
        BDDMockito.given(userCouponRepository.save(any()))
            .willReturn(userCoupon)

        //when
        couponService.isUsed(1L, 1L)

        //then
        Mockito.verify(userCouponRepository, times(1))
            .findByCouponIdAndUserIdAndIsUsed(any(), any(), any())
        Mockito.verify(userCouponRepository, times(1))
            .save(any())
    }

    @DisplayName("쿠폰이 없으면 null을 반환한다.")
    @Test
    fun noCoupon() {
        //when
        couponService.isUsed(null, 1L)

        //then
        Mockito.verify(userCouponRepository, never())
            .findByCouponIdAndUserIdAndIsUsed(any(), any(), any())
        Mockito.verify(userCouponRepository, never())
            .save(any())
    }

    @DisplayName("쿠폰은 존재하지만 사용자가 발행하지 않았다면 예외가 발생한다.")
    @Test
    fun doesNotUserCoupon() {
        //given
        BDDMockito.given(userCouponRepository.findByCouponIdAndUserIdAndIsUsed(any(), any(), any()))
            .willReturn(emptyList())

        //when //then
        assertThatThrownBy { couponService.isUsed(1L, 1L) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("해당 쿠폰을 사용할 수 없습니다. 쿠폰이 존재하지 않거나 이미 사용되었습니다.")

        Mockito.verify(userCouponRepository, times(1))
            .findByCouponIdAndUserIdAndIsUsed(any(), any(), any())
        Mockito.verify(userCouponRepository, never())
            .save(any())
    }

}
