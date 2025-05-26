package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.fixture.point.PointCommandFixture
import kr.hhplus.be.server.fixture.point.PointDomainFixture
import kr.hhplus.be.server.infrastructure.point.PointJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
class PointServiceConcurrencyTest {

    @Autowired
    private lateinit var pointService: PointService

    @Autowired
    private lateinit var pointJpaRepository: PointJpaRepository

    @MockitoBean
    private lateinit var pointEventPublisher: PointEventPublisher

    @AfterEach
    fun tearDown() {
        pointJpaRepository.deleteAllInBatch()
    }

    @DisplayName("포인트를 동시에 충전해도 누락되지 않고 수정된다.")
    @Test
    fun lostChargePoint() {
        //given
        val point = PointDomainFixture.create(pointId = 0L, 100_000)
        val savedPoint = pointJpaRepository.save(point)

        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        //when
        for (idx in 1..threadCount) {
            executorService.execute {
                try {
                    pointService.charge(savedPoint.id, 1000)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val findStock = pointJpaRepository.findById(savedPoint.id)
        assertThat(findStock.get().balance).isEqualTo(200_000)
    }

    @DisplayName("포인트를 동시에 차감해도 누락되지 않고 수정된다.")
    @Test
    fun lostDeductPoint() {
        //given
        val point = PointDomainFixture.create(pointId = 0L, 100_000)
        val savedPoint = pointJpaRepository.save(point)

        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        val command = PointCommandFixture.create(pointId = savedPoint.id, totalPrice = 1000)

        //when
        for (idx in 1..threadCount) {
            executorService.execute {
                try {
                    pointService.use(command)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val findStock = pointJpaRepository.findById(savedPoint.id)
        assertThat(findStock.get().balance).isEqualTo(0)
    }

    @DisplayName("충전과 차감 요청이 동시에 들어와도 데이터 정합성이 보장된다")
    @Test
    fun pointChargeAndUseConcurrencyWithValidation() {
        // given
        val point = PointDomainFixture.create(pointId = 0L, 0)
        val savedPoint = pointJpaRepository.save(point)

        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        val chargeCount = 50
        val useCount = 50

        val successChargeCount = AtomicInteger(0)
        val successUseCount = AtomicInteger(0)

        val command = PointCommandFixture.create(pointId = savedPoint.id, totalPrice = 1000)

        // when
        repeat(chargeCount) {
            executorService.execute {
                try {
                    pointService.charge(savedPoint.id, 1000)
                    successChargeCount.incrementAndGet()
                } finally {
                    latch.countDown()
                }
            }
        }

        repeat(useCount) {
            executorService.execute {
                try {
                    pointService.use(command)
                    successUseCount.incrementAndGet()
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        // then
        val finalPoint = pointJpaRepository.findById(savedPoint.id)
        val expectedBalance = successChargeCount.get() * 1000 - successUseCount.get() * 1000

        assertThat(finalPoint.get().balance).isEqualTo(expectedBalance)
    }

}