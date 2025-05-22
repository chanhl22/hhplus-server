package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.fixture.order.OrderCommandFixture
import kr.hhplus.be.server.fixture.order.OrderDomainFixture
import kr.hhplus.be.server.fixture.order.OrderProductDomainFixture
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

    @Mock
    private lateinit var orderEventPublisher: OrderEventPublisher

    @InjectMocks
    private lateinit var orderService: OrderService

    @DisplayName("통계를 위해 주문을 집계한다.")
    @Test
    fun aggregateOrderProduct() {
        //given
        val orders = listOf(OrderDomainFixture.create())
        BDDMockito.given(orderRepository.findByRegisteredAtBetween(any(), any()))
            .willReturn(orders)

        val orderProducts = listOf(OrderProductDomainFixture.create())
        BDDMockito.given(orderProductRepository.findByOrderIn(any()))
            .willReturn(orderProducts)

        //when
        orderService.aggregateOrderProduct()

        //then
        Mockito.verify(orderRepository, times(1))
            .findByRegisteredAtBetween(any(), any())
        Mockito.verify(orderProductRepository, times(1))
            .findByOrderIn(any())
    }

    @DisplayName("주문을 생성한다.")
    @Test
    fun ready() {
        //given
        val order = OrderDomainFixture.create()
        BDDMockito.given(orderRepository.save(any()))
            .willReturn(order)

        BDDMockito.willDoNothing()
            .given(orderProductRepository)
            .saveAll(any())

        val command = OrderCommandFixture.create()

        BDDMockito.willDoNothing()
            .given(orderEventPublisher)
            .publish(any<OrderEvent.Create>())

        //when
        orderService.ready(command)

        //then
        Mockito.verify(orderRepository, times(1))
            .save(any())
        Mockito.verify(orderProductRepository, times(1))
            .saveAll(any())
        Mockito.verify(orderEventPublisher, times(1))
            .publish(any<OrderEvent.Create>())
    }

}