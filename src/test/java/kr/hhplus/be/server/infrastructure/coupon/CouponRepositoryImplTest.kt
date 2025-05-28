package kr.hhplus.be.server.infrastructure.coupon

import kr.hhplus.be.server.domain.coupon.CouponReserveStatus.ALREADY_REQUESTED
import kr.hhplus.be.server.domain.coupon.CouponReserveStatus.NO_STOCK_INFO
import kr.hhplus.be.server.domain.coupon.CouponReserveStatus.OUT_OF_STOCK
import kr.hhplus.be.server.domain.coupon.DiscountType
import kr.hhplus.be.server.fixture.coupon.CouponDomainFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class CouponRepositoryImplTest {

    @Autowired
    private lateinit var couponRepositoryImpl: CouponRepositoryImpl

    @Autowired
    private lateinit var couponJpaRepository: CouponJpaRepository

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

    @DisplayName("쿠폰을 조회한다.")
    @Test
    fun find() {
        //given
        val coupon = CouponDomainFixture.create(couponId = 0L)
        couponJpaRepository.save(coupon)

        //when
        val result = couponRepositoryImpl.find(coupon.id)

        //then
        assertThat(result)
            .extracting("name", "discountType", "discountValue")
            .containsExactly(
                "1000원 할인 쿠폰", DiscountType.AMOUNT, 1000
            )
    }

    @DisplayName("쿠폰을 저장한다.")
    @Test
    fun save() {
        //given
        val coupon = CouponDomainFixture.create(couponId = 0L)

        //when
        val result = couponRepositoryImpl.save(coupon)

        //then
        assertThat(result)
            .extracting("name", "discountType", "discountValue")
            .containsExactly(
                "1000원 할인 쿠폰", DiscountType.AMOUNT, 1000
            )
    }

    @DisplayName("쿠폰 발급 요청을 저장한다.")
    @Test
    fun reserveFirstCome() {
        //given
        val couponId = 1L
        val userId = 1L

        val couponKey = String.format("coupon:%s:requested:users", couponId)
        val quantityKey = String.format("coupon:%s:quantity", couponId)
        val statusKey = String.format("coupon:%s:user:status", couponId)
        val activeKey = "coupon:active"

        redisTemplate.opsForValue().set(quantityKey, "100")

        //when
        couponRepositoryImpl.reserveFirstCome(couponId, userId)

        //then
        val couponRequestMembers = redisTemplate.opsForSet().members(couponKey)
        val statusValue = redisTemplate.opsForHash<String, String>().get(statusKey, userId.toString())
        val activeCoupons = redisTemplate.opsForSet().members(activeKey)
        val quantity = redisTemplate.opsForValue().get(quantityKey)

        assertThat(couponRequestMembers).contains("1")
        assertThat(statusValue).isEqualTo("pending")
        assertThat(activeCoupons).contains("1")
        assertThat(quantity).isEqualTo("100")
    }

    @DisplayName("쿠폰 발급 요청 수가 수량보다 많으면 저장하지 않고 재고 부족을 반환한다.")
    @Test
    fun overStock() {
        // given
        val couponId = 2L
        val couponKey = "coupon:$couponId:requested:users"
        val quantityKey = "coupon:$couponId:quantity"

        redisTemplate.opsForValue().set(quantityKey, "1")

        // 요청자 1명 미리 등록
        redisTemplate.opsForSet().add(couponKey, "existing_user")

        // when
        val result = couponRepositoryImpl.reserveFirstCome(couponId, 999L)

        // then
        assertThat(result).isEqualTo(OUT_OF_STOCK)
    }

    @DisplayName("쿠폰 발급 중복 요청이면 저장하지 않고 이미 요청한 유저를 반환한다.")
    @Test
    fun duplicateRequest() {
        // given
        val couponId = 3L
        val userId = 123L
        val couponKey = "coupon:$couponId:requested:users"
        val quantityKey = "coupon:$couponId:quantity"

        redisTemplate.opsForValue().set(quantityKey, "10")
        redisTemplate.opsForSet().add(couponKey, userId.toString())

        // when
        val result = couponRepositoryImpl.reserveFirstCome(couponId, userId)

        // then
        assertThat(result).isEqualTo(ALREADY_REQUESTED)
    }

    @DisplayName("쿠폰 수량 정보가 없으면 저장하지 않고 재고 정보 없음을 반환한다.")
    @Test
    fun noStockInfo() {
        // given
        val couponId = 4L
        val userId = 1L
        val quantityKey = "coupon:$couponId:quantity"

        redisTemplate.delete(quantityKey)

        // when
        val result = couponRepositoryImpl.reserveFirstCome(couponId, userId)

        // then
        assertThat(result).isEqualTo(NO_STOCK_INFO)
    }

    private fun redisFlushAll() {
        redisTemplate.connectionFactory?.connection?.serverCommands()?.flushAll()
    }

}