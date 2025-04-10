package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.fixture.order.OrderCommandFixture
import kr.hhplus.be.server.fixture.product.ProductDomainFixture
import kr.hhplus.be.server.fixture.user.UserFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class OrderServiceTest {

    @Mock
    private lateinit var orderRepository: OrderRepository

    @Mock
    private lateinit var orderProductRepository: OrderProductRepository

    @Mock
    private lateinit var orderFactory: OrderFactory

    @DisplayName("주문을 생성한다.")
    @Test
    fun createOrder() {
        //given
        val orderService = OrderService(orderRepository, orderProductRepository, orderFactory)

        val user = UserFixture.create()
        val products = ProductDomainFixture.createProducts()
        val order = Order.create(user, products)
        BDDMockito.given(orderFactory.create(user, products))
            .willReturn(order)
        BDDMockito.given(orderRepository.save(order))
            .willReturn(order)

        BDDMockito.willDoNothing()
            .given(orderProductRepository)
            .saveAll(order.orderProducts)

        //when
        val orderCommand = OrderCommandFixture.create(user, products)
        val result = orderService.createOrder(orderCommand)

        //then
        assertThat(result)
            .extracting("id", "totalPrice")
            .containsExactly(order.id, order.totalPrice)
        Mockito.verify(orderFactory, times(1))
            .create(user, products)
        Mockito.verify(orderRepository, times(1))
            .save(order)
        Mockito.verify(orderProductRepository, times(1))
            .saveAll(order.orderProducts)
    }

}