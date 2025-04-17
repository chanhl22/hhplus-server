package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.fixture.coupon.CouponDomainFixture
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

class CouponTest {

    @DisplayName("쿠폰을 발행할 수 있는지 확인한다.")
    @Test
    fun publish() {
        //given
        val coupon = CouponDomainFixture.create()

        //when //then
        assertThatCode { coupon.publish() }
            .doesNotThrowAnyException()
    }

    @DisplayName("쿠폰 수량이 부족하다면 예외가 발생한다.")
    @Test
    fun isSoldOut() {
        //given
        val coupon = CouponDomainFixture.create(remainingQuantity = 0)

        //when //then
        assertThatThrownBy { coupon.publish() }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("쿠폰이 모두 소진되었습니다.")
    }

    @DisplayName("쿠폰 만료되었다면 예외가 발생한다.")
    @Test
    fun isExpired() {
        //given
        val coupon = CouponDomainFixture.create(expiredAt = LocalDateTime.now().minusDays(1))

        //when //then
        assertThatThrownBy { coupon.publish() }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("쿠폰이 만료되었습니다.")
    }

    @DisplayName("쿠폰의 수량을 감소시킨다.")
    @Test
    fun deduct() {
        //given
        val coupon = CouponDomainFixture.create(remainingQuantity = 10)

        //when
        coupon.deduct()

        // then
        assertThat(coupon.remainingQuantity).isEqualTo(9)
    }

    @DisplayName("쿠폰 수량을 감소시킬 때 수량이 부족하다면 예외가 발생한다.")
    @Test
    fun deductIfSoldOut() {
        //given
        val coupon = CouponDomainFixture.create(remainingQuantity = 0)

        //when //then
        assertThatThrownBy { coupon.publish() }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("쿠폰이 모두 소진되었습니다.")
    }

    @DisplayName("쿠폰을 발행하면 UserCoupon에 기록한다.")
    @Test
    fun issueTo() {
        //given
        val coupon = CouponDomainFixture.create()

        //when
        val userCoupon = coupon.issueTo(coupon.id)

        //then
        assertThat(userCoupon.couponId).isEqualTo(coupon.id)
    }

    @DisplayName("쿠폰이 동시에 발급된다.")
    @Test
    fun deduct2() {
        //given
        val coupon = CouponDomainFixture.create(remainingQuantity = 10)

        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        //when
        val successCount = AtomicInteger(0)
        val failCount = AtomicInteger(0)

        for (idx in 1..threadCount) {
            executorService.execute {
                try {
                    coupon.deduct()
                    successCount.incrementAndGet()
                } catch (e: Exception) {
                    failCount.incrementAndGet()
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then 동시성 테스트가 실패함을 검증
        assertThat(successCount.get()).isGreaterThan(1)
        assertThat(coupon.remainingQuantity).isNotPositive()
    }

}