package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.fixture.coupon.CouponCommandFixture
import kr.hhplus.be.server.fixture.coupon.CouponDomainFixture
import kr.hhplus.be.server.fixture.coupon.UserCouponDomainFixture
import kr.hhplus.be.server.fixture.point.PointDomainFixture
import kr.hhplus.be.server.fixture.user.UserDomainFixture
import kr.hhplus.be.server.infrastructure.coupon.CouponJpaRepository
import kr.hhplus.be.server.infrastructure.coupon.UserCouponJpaRepository
import kr.hhplus.be.server.infrastructure.point.PointJpaRepository
import kr.hhplus.be.server.infrastructure.user.UserJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class CouponServiceIntegrationTest {

    @Autowired
    private lateinit var couponService: CouponService

    @MockitoBean
    private lateinit var couponEventPublisher: CouponEventPublisher

    @Autowired
    private lateinit var couponJpaRepository: CouponJpaRepository

    @Autowired
    private lateinit var userJpaRepository: UserJpaRepository

    @Autowired
    private lateinit var pointJpaRepository: PointJpaRepository

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

    @DisplayName("선착순 쿠폰 발급 요청을 한다.")
    @Test
    fun reserveFirstCome() {
        //given
        val point = PointDomainFixture.create(pointId = 0L, balance = 5_000_000)
        val savedPoint = pointJpaRepository.save(point)

        val user = UserDomainFixture.create(userId = 0L, pointId = savedPoint.id, balance = savedPoint.balance)
        val savedUser = userJpaRepository.save(user)

        val coupon = CouponDomainFixture.create(couponId = 0L, remainingQuantity = 100)
        val savedCoupon = couponJpaRepository.save(coupon)

        val couponKey = String.format("coupon:%s:requested:users", savedCoupon.id)
        val quantityKey = String.format("coupon:%s:quantity", savedCoupon.id)
        val statusKey = String.format("coupon:%s:user:status", savedCoupon.id)
        val activeKey = "coupon:active"

        //when
        couponService.reserveFirstCome(savedCoupon.id, savedUser.id)

        //then
        val couponRequestMembers = redisTemplate.opsForSet().members(couponKey)
        val statusValue = redisTemplate.opsForHash<String, String>().get(statusKey, savedUser.id.toString())
        val activeCoupons = redisTemplate.opsForSet().members(activeKey)
        val quantity = redisTemplate.opsForValue().get(quantityKey)

        assertThat(couponRequestMembers).contains(savedUser.id.toString())
        assertThat(statusValue).isEqualTo("pending")
        assertThat(activeCoupons).contains(savedCoupon.id.toString())
        assertThat(quantity).isEqualTo("100")
    }

    @DisplayName("쿠폰을 발급한다.")
    @Test
    fun issueCoupon() {
        //given
        val user = UserDomainFixture.create(userId = 0L)
        val savedUser = userJpaRepository.save(user)

        val coupon = CouponDomainFixture.create(couponId = 0L)
        val savedCoupon = couponJpaRepository.save(coupon)

        val activeKey = "coupon:active"
        val statusKey = String.format("coupon:%s:user:status", savedCoupon.id)

        redisTemplate.opsForSet().add(activeKey, savedCoupon.id.toString())
        redisTemplate.opsForHash<String, String>().put(statusKey, savedUser.id.toString(), "pending")


        //when
        couponService.issueCoupon()

        //then
        val userCouponResult = userCouponJpaRepository.findByCouponId(savedCoupon.id)
        assertThat(userCouponResult)
            .extracting("userId", "couponId", "isUsed")
            .containsExactly(savedUser.id, savedCoupon.id, false)

        val statusValue = redisTemplate.opsForHash<String, String>().get(statusKey, savedUser.id.toString())
        assertThat(statusValue).isEqualTo("success")
    }

    @DisplayName("쿠폰을 사용한다.")
    @Test
    fun isUsed() {
        //given
        val user = UserDomainFixture.create(userId = 0L)
        val savedUser = userJpaRepository.save(user)

        val coupon = CouponDomainFixture.create(couponId = 0L)
        val savedCoupon = couponJpaRepository.save(coupon)

        val userCoupon = UserCouponDomainFixture.create(
            userCouponId = 0L,
            userId = savedUser.id,
            couponId = savedCoupon.id
        )
        userCouponJpaRepository.save(userCoupon)

        val command = CouponCommandFixture.create(userId = savedUser.id, couponId = savedCoupon.id)

        //when
        couponService.isUsed(command)

        //then
        val userCouponResult = userCouponJpaRepository.findByCouponId(savedCoupon.id)
        assertThat(userCouponResult)
            .extracting("userId", "couponId", "isUsed")
            .containsExactly(savedUser.id, savedCoupon.id, true)
    }

    @DisplayName("쿠폰 발급 상태를 조회한다.")
    @Test
    fun polling() {
        //given
        val user = UserDomainFixture.create(userId = 0L)
        val savedUser = userJpaRepository.save(user)

        val coupon = CouponDomainFixture.create(couponId = 0L)
        val savedCoupon = couponJpaRepository.save(coupon)

        val userCoupon = UserCouponDomainFixture.create(
            userCouponId = 0L,
            userId = savedUser.id,
            couponId = savedCoupon.id
        )
        userCouponJpaRepository.save(userCoupon)

        //when
        couponService.polling(savedCoupon.id, savedUser.id)

        //then
        val userCouponResult = userCouponJpaRepository.findByCouponId(savedCoupon.id)
        assertThat(userCouponResult)
            .extracting("userId", "couponId", "isUsed")
            .containsExactly(savedUser.id, savedCoupon.id, false)
    }

    private fun redisFlushAll() {
        redisTemplate.connectionFactory?.connection?.serverCommands()?.flushAll()
    }

}