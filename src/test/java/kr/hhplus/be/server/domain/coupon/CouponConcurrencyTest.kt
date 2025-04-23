package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.fixture.coupon.CouponDomainFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

class CouponConcurrencyTest {

    @DisplayName("쿠폰이 동시에 차감될 수 없다.")
    @Test
    fun deduct() {
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
        assertThat(successCount.get()).isGreaterThanOrEqualTo(10)
        assertThat(coupon.remainingQuantity).isNotPositive()
    }

}