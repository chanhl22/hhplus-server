package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.fixture.coupon.CouponCommandFixture
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

    @Mock
    private lateinit var couponEventPublisher: CouponEventPublisher

    @InjectMocks
    private lateinit var couponService: CouponService


    @DisplayName("쿠폰을 발행한다.")
    @Test
    fun reserveFirstCome() {
        //given
        val coupon = CouponDomainFixture.create()
        BDDMockito.given(couponRepository.find(any()))
            .willReturn(coupon)

        BDDMockito.willDoNothing()
            .given(couponRepository)
            .registerQuantityKey(any(), any())

        BDDMockito.given(couponRepository.reserveFirstCome(any(), any()))
            .willReturn(CouponReserveStatus.SUCCESS)

        //when
        couponService.reserveFirstCome(1L, 1L)

        //then
        Mockito.verify(couponRepository, times(1))
            .find(any())
        Mockito.verify(couponRepository, times(1))
            .registerQuantityKey(any(), any())
        Mockito.verify(couponRepository, times(1))
            .reserveFirstCome(any(), any())
    }

    @DisplayName("쿠폰을 발행한다.")
    @Test
    fun issueCoupon() {
        //given
        BDDMockito.given(couponRepository.findActiveCoupon())
            .willReturn(setOf("1"))

        BDDMockito.given(couponRepository.updateSuccess(any()))
            .willReturn(emptyList())

        BDDMockito.willDoNothing()
            .given(userCouponRepository)
            .saveAll(any())

        //when
        couponService.issueCoupon()

        //then
        Mockito.verify(couponRepository, times(1))
            .findActiveCoupon()
        Mockito.verify(couponRepository, times(1))
            .updateSuccess(any())
        Mockito.verify(userCouponRepository, times(1))
            .saveAll(any())
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

        BDDMockito.given(userCouponRepository.existsByCouponIdAndUserIdAndIsUsed(any(), any(), any()))
            .willReturn(true)

        val command = CouponCommandFixture.create()

        //when
        couponService.isUsed(command)

        //then
        Mockito.verify(userCouponRepository, times(1))
            .findByCouponIdAndUserIdAndIsUsed(any(), any(), any())
        Mockito.verify(userCouponRepository, times(1))
            .existsByCouponIdAndUserIdAndIsUsed(any(), any(), any())
        Mockito.verify(userCouponRepository, times(1))
            .save(any())
    }

    @DisplayName("쿠폰이 없으면 null을 반환한다.")
    @Test
    fun noCoupon() {
        //given
        val command = CouponCommandFixture.create()

        //when
        couponService.isUsed(command)

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

        BDDMockito.given(userCouponRepository.existsByCouponIdAndUserIdAndIsUsed(any(), any(), any()))
            .willReturn(true)

        val command = CouponCommandFixture.create()

        //when //then
        assertThatThrownBy { couponService.isUsed(command) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("해당 쿠폰을 사용할 수 없습니다. 쿠폰이 존재하지 않거나 이미 사용되었습니다.")

        Mockito.verify(userCouponRepository, times(1))
            .findByCouponIdAndUserIdAndIsUsed(any(), any(), any())
        Mockito.verify(userCouponRepository, times(1))
            .existsByCouponIdAndUserIdAndIsUsed(any(), any(), any())
        Mockito.verify(userCouponRepository, never())
            .save(any())
    }

    @DisplayName("쿠폰 발급 상태를 조회한다.")
    @Test
    fun polling() {
        //given
        BDDMockito.given(userCouponRepository.findByCouponIdAndUserIdAndIsUsed(any(), any(), any()))
            .willReturn(emptyList())

        //when
        couponService.polling(1L, 1L)

        //then
        Mockito.verify(userCouponRepository, times(1))
            .findByCouponIdAndUserIdAndIsUsed(any(), any(), any())
    }

}
