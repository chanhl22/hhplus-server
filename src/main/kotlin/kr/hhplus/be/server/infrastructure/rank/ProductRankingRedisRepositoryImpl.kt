package kr.hhplus.be.server.infrastructure.rank

import kr.hhplus.be.server.domain.rank.ProductRankingRedisRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Repository
class ProductRankingRedisRepositoryImpl(
    private val redisTemplate: StringRedisTemplate
) : ProductRankingRedisRepository {

    companion object {
        private const val RANKING_KEY_DAILY = "product:ranking:daily:%s"
    }

    override fun increaseScores(productIdToCount: Map<Long, Int>) {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val dailyKey = String.format(RANKING_KEY_DAILY, today)

        productIdToCount.forEach { (productId, count) ->
            redisTemplate.opsForZSet().incrementScore(
                dailyKey, productId.toString(), count.toDouble()
            )
        }
    }

}