package kr.hhplus.be.server.infrastructure.order

import kr.hhplus.be.server.fixture.order.OrderDomainFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class OrderRepositoryImplTest {

    @Autowired
    private lateinit var orderRepositoryImpl: OrderRepositoryImpl

    @Autowired
    private lateinit var orderJpaRepository: OrderJpaRepository

    @DisplayName("주문을 저장한다.")
    @Test
    fun save() {
        //given
        val order = OrderDomainFixture.create(orderId = 0L, totalPrice = 100000)

        //when
        val result = orderRepositoryImpl.save(order)

        //then
        assertThat(result.totalPrice).isEqualTo(100000)
    }

}