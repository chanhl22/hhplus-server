package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.fixture.point.PointDomainFixture
import kr.hhplus.be.server.infrastructure.point.PointJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class PointServiceConcurrencyTest {

    @Autowired
    private lateinit var pointService: PointService

    @Autowired
    private lateinit var pointJpaRepository: PointJpaRepository

    @BeforeEach
    fun setUp() {
        val point = PointDomainFixture.create(pointId = 0L, 100_000)
        pointJpaRepository.save(point)
    }

    @AfterEach
    fun tearDown() {
        pointJpaRepository.deleteAllInBatch()
    }

    @DisplayName("포인트가 동시에 충전될 수 없다.")
    @Test
    fun charge() {
        //given
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        //when
        for (idx in 1..threadCount) {
            executorService.execute {
                try {
                    pointService.charge(1L, 1000)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val findStock = pointJpaRepository.findById(1L)
        assertThat(findStock.get().balance).isEqualTo(200_000)
    }

    @DisplayName("포인트가 동시에 차감될 수 없다.")
    @Test
    fun deduct() {
        //given
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        //when
        for (idx in 1..threadCount) {
            executorService.execute {
                try {
                    pointService.use(1L, 1000)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val findStock = pointJpaRepository.findById(1L)
        assertThat(findStock.get().balance).isEqualTo(0)
    }

}