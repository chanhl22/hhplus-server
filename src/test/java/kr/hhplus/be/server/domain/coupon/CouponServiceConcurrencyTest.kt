package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.fixture.coupon.CouponDomainFixture
import kr.hhplus.be.server.fixture.user.UserDomainFixture
import kr.hhplus.be.server.infrastructure.coupon.CouponJpaRepository
import kr.hhplus.be.server.infrastructure.coupon.UserCouponJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
class CouponServiceConcurrencyTest {

    @Autowired
    private lateinit var couponService: CouponService

    @Autowired
    private lateinit var couponJpaRepository: CouponJpaRepository

    @Autowired
    private lateinit var userCouponJpaRepository: UserCouponJpaRepository

    @AfterEach
    fun tearDown() {
        couponJpaRepository.deleteAllInBatch()
        userCouponJpaRepository.deleteAllInBatch()
    }

    @DisplayName("쿠폰 발행을 동시에 요청해도 누락되지 않고 차감된다.")
    @Test
    fun lostIssueCoupon() {
        //given
        val coupon = CouponDomainFixture.create(couponId = 0L, remainingQuantity = 100)
        val savedCoupon = couponJpaRepository.save(coupon)

        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        //when
        for (idx in 1..threadCount) {
            executorService.execute {
                try {
                    couponService.issueCoupon(savedCoupon.id, 1L)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val findCoupon = couponJpaRepository.findById(savedCoupon.id)
        assertThat(findCoupon.get().remainingQuantity).isEqualTo(0)
    }

    @DisplayName("동시에 두 명이 동일한 쿠폰을 발급받으면 재고가 음수가 되지 않아야 한다.")
    @Test
    fun couponRemainingQuantityAlwaysPositive() {
        //given
        val user = UserDomainFixture.create(userId = 0L)

        val coupon = CouponDomainFixture.create(couponId = 0L, remainingQuantity = 1)
        val savedCoupon = couponJpaRepository.save(coupon)

        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(2)

        val exceptionCount = AtomicInteger(0)

        //when
        for (idx in 1..2) {
            executorService.execute {
                try {
                    couponService.issueCoupon(savedCoupon.id, user.id)
                } catch (e: IllegalStateException) {
                    exceptionCount.incrementAndGet()
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val findCoupon = couponJpaRepository.findById(savedCoupon.id)
        assertThat(findCoupon.get().remainingQuantity).isEqualTo(0)
        assertThat(exceptionCount.get()).isEqualTo(1)
    }

}