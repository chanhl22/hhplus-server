package kr.hhplus.be.server.infrastructure.point

import kr.hhplus.be.server.fixture.point.PointDomainFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class PointRepositoryImplTest {

    @Autowired
    private lateinit var pointRepositoryImpl: PointRepositoryImpl

    @Autowired
    private lateinit var pointJpaRepository: PointJpaRepository

    @DisplayName("포인트를 조회한다.")
    @Test
    fun find() {
        //given
        val point = PointDomainFixture.create(pointId = 0L, balance = 10000)
        val savedPoint = pointJpaRepository.save(point)

        //when
        val result = pointRepositoryImpl.find(savedPoint.id)

        //then
        assertThat(result.balance).isEqualTo(10000)
    }

    @DisplayName("포인트를 저장한다.")
    @Test
    fun save() {
        //given
        val point = PointDomainFixture.create(pointId = 0L, balance = 10000)
        val savedPoint = pointJpaRepository.save(point)

        //when
        val result = pointRepositoryImpl.save(savedPoint)

        //then
        assertThat(result.balance).isEqualTo(10000)
    }

}