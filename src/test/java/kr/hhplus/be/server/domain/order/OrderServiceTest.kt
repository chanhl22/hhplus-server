package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.fixture.order.OrderCommandFixture
import kr.hhplus.be.server.fixture.order.OrderDomainFixture
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any

@ExtendWith(MockitoExtension::class)
class OrderServiceTest {

    @Mock
    private lateinit var orderRepository: OrderRepository

    @Mock
    private lateinit var orderProductRepository: OrderProductRepository

    @InjectMocks
    private lateinit var orderService: OrderService

    @DisplayName("주문을 생성한다.")
    @Test
    fun order() {
        //given
        val order = OrderDomainFixture.create()
        BDDMockito.given(orderRepository.save(any()))
            .willReturn(order)

        BDDMockito.willDoNothing()
            .given(orderProductRepository)
            .saveAll(any())

        //when
        val orderCommand = OrderCommandFixture.create()
        orderService.order(orderCommand)

        //then
        Mockito.verify(orderRepository, times(1))
            .save(any())
        Mockito.verify(orderProductRepository, times(1))
            .saveAll(any())
    }

}