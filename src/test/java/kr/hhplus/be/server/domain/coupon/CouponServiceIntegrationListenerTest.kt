package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.fixture.coupon.CouponDomainFixture
import kr.hhplus.be.server.infrastructure.coupon.CouponJpaRepository
import kr.hhplus.be.server.infrastructure.coupon.UserCouponJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.shaded.org.awaitility.Awaitility.await
import java.time.Duration
import java.util.concurrent.TimeUnit

@Transactional
@SpringBootTest
class CouponServiceIntegrationListenerTest {

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
        redisFlushAll()
    }

    @DisplayName("선착순 쿠폰 이벤트를 발행하면 쿠폰이 발급된다.")
    @Test
    fun checkIssueCoupon() {
        //given
        val coupon = CouponDomainFixture.create(couponId = 0L, remainingQuantity = 100)
        val savedCoupon = couponJpaRepository.save(coupon)

        //when
        for (idx in 1..100) {
            couponService.reserveFirstCome(savedCoupon.id, idx.toLong())
        }

        //then
        await()
            .pollInterval(Duration.ofMillis(500))
            .atMost(30, TimeUnit.SECONDS)
            .untilAsserted {
                val findUserCoupon = userCouponJpaRepository.findAll()
                assertThat(findUserCoupon).hasSize(100)
                    .extracting("userId", "couponId", "isUsed")
                    .containsAnyOf(
                        Tuple.tuple(1L, savedCoupon.id, false),
                        Tuple.tuple(2L, savedCoupon.id, false)
                    )
            }
    }

    private fun redisFlushAll() {
        redisTemplate.connectionFactory?.connection?.serverCommands()?.flushAll()
    }

}