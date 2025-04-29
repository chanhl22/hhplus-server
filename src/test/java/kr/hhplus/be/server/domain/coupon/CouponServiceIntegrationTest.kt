package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.fixture.coupon.CouponDomainFixture
import kr.hhplus.be.server.fixture.coupon.UserCouponDomainFixture
import kr.hhplus.be.server.fixture.user.UserDomainFixture
import kr.hhplus.be.server.infrastructure.coupon.CouponJpaRepository
import kr.hhplus.be.server.infrastructure.coupon.UserCouponJpaRepository
import kr.hhplus.be.server.infrastructure.user.UserJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class CouponServiceIntegrationTest {

    @Autowired
    private lateinit var couponService: CouponService

    @Autowired
    private lateinit var couponJpaRepository: CouponJpaRepository

    @Autowired
    private lateinit var userJpaRepository: UserJpaRepository

    @Autowired
    private lateinit var userCouponJpaRepository: UserCouponJpaRepository

    @DisplayName("쿠폰을 조회한다.")
    @Test
    fun find() {
        //given
        val user = UserDomainFixture.create(userId = 0L)
        val savedUser = userJpaRepository.save(user)

        val coupon = CouponDomainFixture.create(couponId = 0L)
        val savedCoupon = couponJpaRepository.save(coupon)

        val userCoupon =
            UserCouponDomainFixture.create(userCouponId = 0L, userId = savedUser.id, couponId = savedCoupon.id)
        userCouponJpaRepository.save(userCoupon)

        //when
        val result = couponService.find(couponId = savedCoupon.id, userId = savedUser.id)

        //then
        assertThat(result).isNotNull
        assertThat(result)
            .extracting("name", "discountType", "discountValue", "remainingQuantity", "expiredAt")
            .containsExactly("1000원 할인 쿠폰", DiscountType.AMOUNT, 1000, 20, coupon.expiredAt)
    }

    @DisplayName("쿠폰을 발행한다.")
    @Test
    fun issueCoupon() {
        //given
        val user = UserDomainFixture.create(userId = 0L)
        val savedUser = userJpaRepository.save(user)

        val coupon = CouponDomainFixture.create(couponId = 0L)
        val savedCoupon = couponJpaRepository.save(coupon)

        //when
        val result = couponService.issueCoupon(savedCoupon.id, user.id)

        //then
        assertThat(result)
            .extracting("name", "discountType", "discountValue", "remainingQuantity", "expiredAt")
            .containsExactly("1000원 할인 쿠폰", DiscountType.AMOUNT, 1000, 19, coupon.expiredAt)

        val userCouponResult = userCouponJpaRepository.findByCouponId(savedCoupon.id)
        assertThat(userCouponResult)
            .extracting("userId", "couponId", "isUsed")
            .containsExactly(savedUser.id, savedCoupon.id, false)
    }

    @DisplayName("쿠폰을 사용한다.")
    @Test
    fun isUsed() {
        //given
        val user = UserDomainFixture.create(userId = 0L)
        val savedUser = userJpaRepository.save(user)

        val coupon = CouponDomainFixture.create(couponId = 0L)
        val savedCoupon = couponJpaRepository.save(coupon)

        couponService.issueCoupon(savedCoupon.id, savedUser.id, )

        //when
        couponService.isUsed(savedCoupon.id, user.id)

        //then
        val userCouponResult = userCouponJpaRepository.findByCouponId(savedCoupon.id)
        assertThat(userCouponResult)
            .extracting("userId", "couponId", "isUsed")
            .containsExactly(savedUser.id, savedCoupon.id, true)
    }

}