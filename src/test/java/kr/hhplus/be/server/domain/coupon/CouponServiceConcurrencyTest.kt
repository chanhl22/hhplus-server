package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.fixture.coupon.CouponDomainFixture
import kr.hhplus.be.server.infrastructure.coupon.CouponJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class CouponServiceConcurrencyTest {

    @Autowired
    private lateinit var couponService: CouponService

    @Autowired
    private lateinit var couponJpaRepository: CouponJpaRepository

    @BeforeEach
    fun setUp() {
        val coupon = CouponDomainFixture.create(couponId = 0L, remainingQuantity = 100)
        couponJpaRepository.save(coupon)
    }

    @AfterEach
    fun tearDown() {
        couponJpaRepository.deleteAllInBatch()
    }

    @DisplayName("쿠폰이 동시에 차감될 수 없다.")
    @Test
    fun issueCoupon() {
        //given
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        //when
        for (idx in 1..threadCount) {
            executorService.execute {
                try {
                    couponService.issueCoupon(1L, 1L)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val findStock = couponJpaRepository.findById(1L)
        assertThat(findStock.get().remainingQuantity).isEqualTo(0)
    }

}