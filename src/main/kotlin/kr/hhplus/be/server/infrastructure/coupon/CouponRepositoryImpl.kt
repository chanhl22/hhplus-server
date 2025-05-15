package kr.hhplus.be.server.infrastructure.coupon

import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.coupon.CouponRepository
import kr.hhplus.be.server.domain.coupon.CouponReserveStatus
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.DefaultRedisScript
import org.springframework.stereotype.Repository

@Repository
class CouponRepositoryImpl(
    private val couponJpaRepository: CouponJpaRepository,
    private val redisTemplate: StringRedisTemplate
) : CouponRepository {

    companion object {
        private const val COUPON_REQUEST_KEY = "coupon:%s:requested:users"
        private const val COUPON_QUANTITY_KEY = "coupon:%s:quantity"
        private const val COUPON_USER_STATUS_KEY = "coupon:%s:user:status"
        private const val COUPON_ACTIVE_KEY = "coupon:active"
    }

    override fun find(couponId: Long): Coupon {
        return couponJpaRepository.findById(couponId)
            .orElseThrow(::IllegalArgumentException)
    }

    override fun findWithPessimisticLock(couponId: Long): Coupon {
        return couponJpaRepository.findWithPessimisticLock(couponId)
            .orElseThrow(::IllegalArgumentException)
    }

    override fun save(coupon: Coupon): Coupon {
        return couponJpaRepository.save(coupon)
    }

    override fun reserveFirstCome(couponId: Long, userId: Long): CouponReserveStatus {
        val couponKey = createKey(COUPON_REQUEST_KEY, couponId)
        val quantityKey = createKey(COUPON_QUANTITY_KEY, couponId)
        val statusKey = createKey(COUPON_USER_STATUS_KEY, couponId)
        val activeKey = COUPON_ACTIVE_KEY

        val luaScript = getLuaScript()
        val result = redisTemplate.execute(
            DefaultRedisScript(luaScript, Long::class.java),
            listOf(couponKey, quantityKey, statusKey, activeKey),
            userId.toString(),
            couponId.toString()
        )

        return CouponReserveStatus.from(result.toInt())
    }

    private fun getLuaScript(): String {
        return """
            local userId = ARGV[1]
            local couponId = ARGV[2]
        
            -- 중복 요청 체크
            if redis.call("SISMEMBER", KEYS[1], userId) == 1 then
                return 0 -- 이미 요청한 유저
            end
        
            -- 재고 확인
            local stock = tonumber(redis.call("GET", KEYS[2]) or "-1")
            if stock < 0 then
                return -2 -- 재고 정보 없음
            end
        
            -- 현재 요청 수와 비교
            local requestedCount = redis.call("SCARD", KEYS[1])
            if (stock - requestedCount) <= 0 then
                return -1 -- 재고 부족
            end
        
            -- 요청 등록 및 상태 저장
            redis.call("SADD", KEYS[1], userId)
            redis.call("HSET", KEYS[3], userId, "pending")
            
            -- 활성 쿠폰 리스트에 쿠폰 ID 등록
            redis.call("SADD", KEYS[4], couponId)
            
            return 1
        """.trimIndent()
    }

    override fun alreadyIssue(couponId: Long, userId: Long): Boolean {
        val couponKey = createKey(COUPON_REQUEST_KEY, couponId)
        return redisTemplate.opsForSet().isMember(couponKey, userId.toString()) ?: false
    }

    override fun existsQuantityKey(couponId: Long): Boolean {
        val quantityKey = createKey(COUPON_QUANTITY_KEY, couponId)
        return redisTemplate.hasKey(quantityKey)
    }

    override fun registerQuantityKey(couponId: Long, remainingQuantity: Int) {
        val quantityKey = createKey(COUPON_QUANTITY_KEY, couponId)
        redisTemplate.opsForValue().set(quantityKey, remainingQuantity.toString())
    }

    override fun findActiveCoupon(): Set<String> {
        return redisTemplate.opsForSet().members(COUPON_ACTIVE_KEY) ?: emptySet()
    }

    override fun updateSuccess(couponId: String): List<String> {
        val statusKey = createKey(COUPON_USER_STATUS_KEY, couponId)
        val hashOps = redisTemplate.opsForHash<String, String>()

        val users = hashOps.entries(statusKey)
            .filterValues { it == "pending" }
            .map { it.key }

        if (users.isNotEmpty()) {
            users.forEach { userId ->
                hashOps.put(statusKey, userId, "success")
            }
        }

        return users
    }

    private fun createKey(key: String, couponId: Any): String {
        return String.format(key, couponId)
    }

}
