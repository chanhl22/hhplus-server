package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.fixture.coupon.CouponDomainFixture
import kr.hhplus.be.server.infrastructure.coupon.CouponJpaRepository
import kr.hhplus.be.server.infrastructure.coupon.UserCouponJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class CouponServiceConcurrencyTest {

    @Autowired
    private lateinit var couponService: CouponService

    @Autowired
    private lateinit var couponJpaRepository: CouponJpaRepository

    @Autowired
    private lateinit var userCouponJpaRepository: UserCouponJpaRepository

    @Autowired
    private lateinit var redisTemplate: StringRedisTemplate

    @BeforeEach
    fun setUp() {
        redisFlushAll()
    }

    @AfterEach
    fun tearDown() {
        couponJpaRepository.deleteAllInBatch()
        userCouponJpaRepository.deleteAllInBatch()
        redisFlushAll()
    }

    @DisplayName("쿠폰 발급 요청을 동시에 해도 쿠폰 수량 만큼 차감된다.")
    @Test
    fun reserveFirstCome() {
        //given
        val coupon = CouponDomainFixture.create(couponId = 0L, remainingQuantity = 100)
        val savedCoupon = couponJpaRepository.save(coupon)

        val threadCount = 200
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        //when
        for (idx in 1..threadCount) {
            executorService.execute {
                try {
                    couponService.reserveFirstCome(savedCoupon.id, idx.toLong())
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val couponKey = String.format("coupon:%s:requested:users", savedCoupon.id)
        val couponRequestMembers = redisTemplate.opsForSet().members(couponKey)!!

        assertThat(couponRequestMembers.size).isEqualTo(100)
    }

    @DisplayName("동시에 동일한 쿠폰을 발급 받을 수 없다.")
    @Test
    fun checkAlreadyRequested() {
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
                    couponService.reserveFirstCome(savedCoupon.id, 1L)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val couponKey = String.format("coupon:%s:requested:users", savedCoupon.id)
        val couponRequestMembers = redisTemplate.opsForSet().members(couponKey)!!

        assertThat(couponRequestMembers.size).isEqualTo(1)
    }

    @DisplayName("쿠폰 수량이 없다면 발급 받을 수 없다.")
    @Test
    fun checkOutOfStock() {
        //given
        val coupon = CouponDomainFixture.create(couponId = 0L, remainingQuantity = 0)
        val savedCoupon = couponJpaRepository.save(coupon)

        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        //when
        for (idx in 1..threadCount) {
            executorService.execute {
                try {
                    couponService.reserveFirstCome(savedCoupon.id, idx.toLong())
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val couponKey = String.format("coupon:%s:requested:users", savedCoupon.id)
        val couponRequestMembers = redisTemplate.opsForSet().members(couponKey)!!

        assertThat(couponRequestMembers.size).isEqualTo(0)
    }

    private fun redisFlushAll() {
        redisTemplate.connectionFactory?.connection?.serverCommands()?.flushAll()
    }

}