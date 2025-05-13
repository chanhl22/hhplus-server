package kr.hhplus.be.server.domain.rank

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

    @DisplayName("일간 상품 랭킹을 조회한다.")
    @Test
    fun findProductRanking() {
        //given
        val limit = 2L
        val key = "product:ranking:daily:%s"
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val dailyKey = String.format(key, today)

        redisTemplate.opsForZSet().add(dailyKey, "1", 1.0)
        redisTemplate.opsForZSet().add(dailyKey, "2", 2.0)

        //when
        val result = productRankingService.findProductRanking(limit)

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