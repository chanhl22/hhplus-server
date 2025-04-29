package kr.hhplus.be.server.infrastructure.product

import kr.hhplus.be.server.fixture.product.ProductStatisticsFixture
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Transactional
@SpringBootTest
class ProductStatisticsRepositoryImplTest {

    @Autowired
    private lateinit var productStatisticsRepositoryImpl: ProductStatisticsRepositoryImpl

    @Autowired
    private lateinit var productStatisticsJpaRepository: ProductStatisticsJpaRepository

    @DisplayName("상품 통계를 조회한다.")
    @Test
    fun findAllByCreatedAtBetween() {
        //given
        val productStatistics = ProductStatisticsFixture.create(
            productStatisticsId = 0L,
            productId = 1L,
            totalSales = 1000,
            createdAt = LocalDateTime.now().minusDays(1)
        )
        productStatisticsJpaRepository.save(productStatistics)

        val startDatetime = LocalDateTime.of(LocalDate.now().minusDays(3), LocalTime.of(0, 0, 0))
        val endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59))

        //when
        val result = productStatisticsRepositoryImpl.findAllByCreatedAtBetween(startDatetime, endDatetime)

        //then
        assertThat(result).hasSize(1)
            .extracting("productId", "totalSales")
            .containsExactly(
                Tuple.tuple(1L, 1000)
            )
    }

    @DisplayName("상품을 저장한다.")
    @Test
    fun saveAll() {
        //given
        val productStatistics = listOf(
            ProductStatisticsFixture.create(
                productStatisticsId = 0L,
                productId = 1L,
                totalSales = 1000,
                createdAt = LocalDateTime.now().minusDays(1)
            )
        )

        //when
        val result = productStatisticsRepositoryImpl.saveAll(productStatistics)

        //then
        val findProductStatistics = productStatisticsJpaRepository.findById(result[0].id)
        assertThat(findProductStatistics.get())
            .extracting("productId", "totalSales")
            .containsExactly(
                1L, 1000
            )
    }

}