package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.fixture.point.PointCommandFixture
import kr.hhplus.be.server.fixture.point.PointDomainFixture
import kr.hhplus.be.server.infrastructure.point.PointJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class PointServiceIntegrationTest {

    @Autowired
    private lateinit var pointService: PointService

    @Autowired
    private lateinit var pointJpaRepository: PointJpaRepository

    @MockitoBean
    private lateinit var pointEventPublisher: PointEventPublisher

    @DisplayName("유저의 포인트를 조회한다.")
    @Test
    fun find() {
        //given
        val point = PointDomainFixture.create(pointId = 0L, balance = 10000)
        val savedPoint = pointJpaRepository.save(point)

        //when
        val result = pointService.find(savedPoint.id)

        //then
        assertThat(result.balance).isEqualTo(10000)
    }

    @DisplayName("유저의 포인트를 충전한다.")
    @Test
    fun charge() {
        //given
        val point = PointDomainFixture.create(pointId = 0L, balance = 10000)
        val savedPoint = pointJpaRepository.save(point)

        //when
        val result = pointService.charge(savedPoint.id, 10000)

        //then
        assertThat(result.balance).isEqualTo(20000)
    }

    @DisplayName("유저의 포인트를 사용한다.")
    @Test
    fun use() {
        //given
        val point = PointDomainFixture.create(pointId = 0L, balance = 10000)
        val savedPoint = pointJpaRepository.save(point)

        val command = PointCommandFixture.create(pointId = savedPoint.id, totalPrice = 10000)

        //when
        pointService.use(command)

        //then
        val findPoint = pointJpaRepository.findById(savedPoint.id).get()
        assertThat(findPoint.balance).isEqualTo(0)
    }

}