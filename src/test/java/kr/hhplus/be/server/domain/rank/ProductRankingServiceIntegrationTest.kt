package kr.hhplus.be.server.domain.rank

import kr.hhplus.be.server.fixture.rank.ProductRankingCommandFixture
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
class ProductRankingServiceIntegrationTest {

    @Autowired
    private lateinit var productRankingService: ProductRankingService

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

    @DisplayName("주문시 상품 랭킹을 업데이트한다.")
    @Test
    fun upsertRanking() {
        //given
        val command = ProductRankingCommandFixture.create(
            mapOf(
                1L to ("상품1" to 3),
                2L to ("상품2" to 5),
                3L to ("상품3" to 2)
            )
        )

        val now = LocalDate.now()

        val dailyPattern = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val dailyKey = "product:ranking:daily:${dailyPattern}"
        val nameDailyKey = "product:name:daily:${dailyPattern}"

        val week = now.get(WeekFields.ISO.weekOfWeekBasedYear())
        val year = now.year
        val weeklyPattern = "$year-W$week"
        val weeklyKey = "product:ranking:weekly:${weeklyPattern}"
        val nameWeeklyKey = "product:name:weekly:${weeklyPattern}"

        //when
        productRankingService.upsertRanking(command)

        //then
        val dailyScores = redisTemplate.opsForZSet().rangeWithScores(dailyKey, 0, -1)
        assertThat(dailyScores)
            .hasSize(3)
            .extracting("value", "score")
            .containsExactly(
                tuple("3", 2.0),
                tuple("1", 3.0),
                tuple("2", 5.0)
            )
        val dailyName = redisTemplate.opsForHash<String, String>().entries(nameDailyKey)
        assertThat(dailyName).containsExactlyInAnyOrderEntriesOf(
            mapOf(
                "1" to "상품1",
                "2" to "상품2",
                "3" to "상품3"
            )
        )
        val weeklyScores = redisTemplate.opsForZSet().rangeWithScores(weeklyKey, 0, -1)
        assertThat(weeklyScores)
            .hasSize(3)
            .extracting("value", "score")
            .containsExactly(
                tuple("3", 2.0),
                tuple("1", 3.0),
                tuple("2", 5.0)
            )
        val weeklyName = redisTemplate.opsForHash<String, String>().entries(nameWeeklyKey)
        assertThat(weeklyName).containsExactlyInAnyOrderEntriesOf(
            mapOf(
                "1" to "상품1",
                "2" to "상품2",
                "3" to "상품3"
            )
        )
    }

    @DisplayName("일간 상품 랭킹을 조회한다.")
    @Test
    fun findProductDailyRanking() {
        //given
        val limit = 2L
        val now = LocalDate.now()
        val dailyPattern = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val dailyKey = "product:ranking:daily:${dailyPattern}"
        val nameKey = "product:name:daily:${dailyPattern}"

        redisTemplate.opsForZSet().add(dailyKey, "1", 1.0)
        redisTemplate.opsForZSet().add(dailyKey, "2", 2.0)
        redisTemplate.opsForHash<String, String>()
            .put(nameKey, "1", "상품1")
        redisTemplate.opsForHash<String, String>()
            .put(nameKey, "2", "상품2")

        //when
        val result = productRankingService.findProductDailyRanking(limit)

        //then
        assertThat(result).hasSize(2)
            .extracting("productId", "productName", "rank")
            .containsExactly(
                tuple(2L, "상품2", 1),
                tuple(1L, "상품1", 2)
            )
    }

    @DisplayName("주간 상품 랭킹을 조회한다.")
    @Test
    fun findProductWeeklyRanking() {
        //given
        val limit = 2L
        val now = LocalDate.now()
        val week = now.get(WeekFields.ISO.weekOfWeekBasedYear())
        val year = now.year
        val weeklyPattern = "$year-W$week"
        val weeklyKey = "product:ranking:weekly:${weeklyPattern}"
        val nameKey = "product:name:weekly:${weeklyPattern}"

        redisTemplate.opsForZSet().add(weeklyKey, "1", 1.0)
        redisTemplate.opsForZSet().add(weeklyKey, "2", 2.0)
        redisTemplate.opsForHash<String, String>()
            .put(nameKey, "1", "상품1")
        redisTemplate.opsForHash<String, String>()
            .put(nameKey, "2", "상품2")

        //when
        val result = productRankingService.findProductWeeklyRanking(limit)

        //then
        assertThat(result).hasSize(2)
            .extracting("productId", "productName", "rank")
            .containsExactly(
                tuple(2L, "상품2", 1),
                tuple(1L, "상품1", 2)
            )
    }

    private fun redisFlushAll() {
        redisTemplate.connectionFactory?.connection?.serverCommands()?.flushAll()
    }

}