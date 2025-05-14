package kr.hhplus.be.server.infrastructure.rank

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple.tuple
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields

@SpringBootTest
class ProductRankingRepositoryImplTest {

    @Autowired
    private lateinit var redisTemplate: StringRedisTemplate

    @Autowired
    private lateinit var productRankingRedisRepositoryImpl: ProductRankingRepositoryImpl

    @BeforeEach
    fun setUp() {
        redisFlushAll()
    }

    @AfterEach
    fun tearDown() {
        redisFlushAll()
    }

    @DisplayName("주문한 상품의 일간 랭킹을 증가시킨다. 조회를 위해 상품 이름도 저장한다.")
    @Test
    fun increaseDailyRanking() {
        //given
        val now = LocalDate.now()
        val productId = 1L
        val productName = "상품1"
        val productQuantity = 10

        val dailyPattern = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val dailyKey = "product:ranking:daily:${dailyPattern}"
        val nameKey = "product:name:daily:${dailyPattern}"

        //when
        productRankingRedisRepositoryImpl.increaseDailyRanking(now, productId, productName, productQuantity)

        //then
        val score = redisTemplate.opsForZSet().score(dailyKey, productId.toString())
        val name = redisTemplate.opsForHash<String, String>()[nameKey, productId.toString()]
        val rankingTtl = redisTemplate.getExpire(dailyKey)
        val nameTtl = redisTemplate.getExpire(nameKey)

        assertThat(score).isEqualTo(productQuantity.toDouble())
        assertThat(name).isEqualTo(productName)
        assertThat(rankingTtl).isGreaterThan(0L)
        assertThat(nameTtl).isGreaterThan(0L)
    }

    @DisplayName("주문한 상품의 주간 랭킹을 증가시킨다. 조회를 위해 상품 이름도 저장한다.")
    @Test
    fun increaseWeeklyRanking() {
        //given
        val now = LocalDate.now()
        val productId = 1L
        val productName = "상품1"
        val productQuantity = 10

        val week = now.get(WeekFields.ISO.weekOfWeekBasedYear())
        val year = now.year
        val weeklyPattern = "$year-W$week"
        val weeklyKey = "product:ranking:weekly:${weeklyPattern}"
        val nameKey = "product:name:weekly:${weeklyPattern}"

        //when
        productRankingRedisRepositoryImpl.increaseWeeklyRanking(now, productId, productName, productQuantity)

        //then
        val score = redisTemplate.opsForZSet().score(weeklyKey, productId.toString())
        val name = redisTemplate.opsForHash<String, String>()[nameKey, productId.toString()]
        val rankingTtl = redisTemplate.getExpire(weeklyKey)
        val nameTtl = redisTemplate.getExpire(nameKey)

        assertThat(score).isEqualTo(productQuantity.toDouble())
        assertThat(name).isEqualTo(productName)
        assertThat(rankingTtl).isGreaterThan(0L)
        assertThat(nameTtl).isGreaterThan(0L)
    }

    @DisplayName("일간 상품 랭킹을 조회한다.")
    @Test
    fun findDailyTop() {
        //given
        val key = "product:ranking:daily:%s"
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val dailyKey = String.format(key, today)

        redisTemplate.opsForZSet().add(dailyKey, "1", 1.0)
        redisTemplate.opsForZSet().add(dailyKey, "2", 2.0)

        //when
        val result = productRankingRedisRepositoryImpl.findDailyTop(2)

        //then
        assertThat(result).hasSize(2)
            .extracting("productId", "rank")
            .containsExactly(
                tuple(2L, 1),
                tuple(1L, 2)
            )
    }

    private fun redisFlushAll() {
        redisTemplate.connectionFactory?.connection?.serverCommands()?.flushAll()
    }

}
