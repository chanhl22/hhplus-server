package kr.hhplus.be.server.infrastructure.order

import kr.hhplus.be.server.fixture.order.OrderDomainFixture
import kr.hhplus.be.server.fixture.order.OrderProductDomainFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class OrderProductRepositoryImplTest {

    @Autowired
    private lateinit var orderProductRepositoryImpl: OrderProductRepositoryImpl

    @Autowired
    private lateinit var orderProductJpaRepository: OrderProductJpaRepository

    @Autowired
    private lateinit var orderJpaRepository: OrderJpaRepository

    @DisplayName("주문 상품을 저장한다.")
    @Test
    fun saveAll() {
        //given
        val order = OrderDomainFixture.create(orderId = 0L, totalPrice = 10000)
        val savedOrder = orderJpaRepository.save(order)
        val orderProduct = OrderProductDomainFixture.create(savedOrder, 1L)

        //when
        orderProductRepositoryImpl.saveAll(listOf(orderProduct))

        //then
        val findOrderProduct = orderProductJpaRepository.findById(orderProduct.id)
        assertThat(findOrderProduct.get().order.totalPrice).isEqualTo(10000)
    }

}