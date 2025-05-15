package kr.hhplus.be.server.infrastructure.rank

import kr.hhplus.be.server.domain.rank.ProductRankingRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields

@Repository
class ProductRankingRepositoryImpl(
    private val redisTemplate: StringRedisTemplate
) : ProductRankingRepository {

    companion object {
        private const val RANKING_DAILY_KEY = "product:ranking:daily:%s"
        private const val RANKING_WEEKLY_KEY = "product:ranking:weekly:%s"
        private const val PRODUCT_NAME_DAILY_KEY = "product:name:daily:%s"
        private const val PRODUCT_NAME_WEEKLY_KEY = "product:name:weekly:%s"
    }

    override fun increaseDailyRanking(now: LocalDate, productId: Long, name: String, quantity: Int) {
        val dailyKey = String.format(RANKING_DAILY_KEY, getDailyPattern(now))
        val dailyNameKey = String.format(PRODUCT_NAME_DAILY_KEY, getDailyPattern(now))

        redisTemplate.opsForZSet()
            .incrementScore(dailyKey, productId.toString(), quantity.toDouble())
        redisTemplate.opsForHash<String, String>()
            .put(dailyNameKey, productId.toString(), name)

        redisTemplate.expire(dailyKey, Duration.ofDays(2))
        redisTemplate.expire(dailyNameKey, Duration.ofDays(2))
    }

    override fun increaseWeeklyRanking(now: LocalDate, productId: Long, name: String, quantity: Int) {
        val weeklyKey = String.format(RANKING_WEEKLY_KEY, getWeeklyPattern(now))
        val weeklyNameKey = String.format(PRODUCT_NAME_WEEKLY_KEY, getWeeklyPattern(now))

        redisTemplate.opsForZSet()
            .incrementScore(weeklyKey, productId.toString(), quantity.toDouble())
        redisTemplate.opsForHash<String, String>()
            .put(weeklyNameKey, productId.toString(), name)

        redisTemplate.expire(weeklyKey, Duration.ofDays(8))
        redisTemplate.expire(weeklyNameKey, Duration.ofDays(8))
    }

    override fun findDailyTopRank(now: LocalDate, limit: Long): List<Pair<String, Int>> {
        val dailyKey = String.format(RANKING_DAILY_KEY, getDailyPattern(now))

        val result = redisTemplate.opsForZSet()
            .reverseRangeWithScores(dailyKey, 0, limit - 1) ?: return emptyList()

        return result.mapIndexed { index, tuple ->
            Pair(tuple.value.toString(), index)
        }
    }

    override fun findWeeklyTopRank(now: LocalDate, limit: Long): List<Pair<String, Int>> {
        val weeklyKey = String.format(RANKING_WEEKLY_KEY, getWeeklyPattern(now))

        val result = redisTemplate.opsForZSet()
            .reverseRangeWithScores(weeklyKey, 0, limit - 1) ?: return emptyList()

        return result.mapIndexed { index, tuple ->
            Pair(tuple.value.toString(), index)
        }
    }

    override fun findDailyProductNames(now: LocalDate, productIds: List<String>): Map<String, String> {
        val dailyNameKey = String.format(PRODUCT_NAME_DAILY_KEY, getDailyPattern(now))

        val productNames = redisTemplate.opsForHash<String, String>()
            .multiGet(dailyNameKey, productIds)

        return productIds
            .zip(productNames)
            .associate { (id, value) -> id to value.toString() }
    }

    override fun findWeeklyProductNames(now: LocalDate, productIds: List<String>): Map<String, String> {
        val weeklyNameKey = String.format(PRODUCT_NAME_WEEKLY_KEY, getWeeklyPattern(now))

        val productNames = redisTemplate.opsForHash<String, String>()
            .multiGet(weeklyNameKey, productIds)

        return productIds
            .zip(productNames)
            .associate { (id, value) -> id to value.toString() }
    }

    private fun getDailyPattern(today: LocalDate): String {
        return today.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
    }

    private fun getWeeklyPattern(date: LocalDate): String {
        val week = date.get(WeekFields.ISO.weekOfWeekBasedYear())
        val year = date.year
        return "$year-W$week"
    }

}