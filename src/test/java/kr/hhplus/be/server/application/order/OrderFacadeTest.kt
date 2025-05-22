package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.user.UserService
import kr.hhplus.be.server.fixture.order.OrderCriteriaFixture
import kr.hhplus.be.server.fixture.user.UserDomainFixture
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
class OrderFacadeTest {

    @Mock
    private lateinit var orderService: OrderService

    @Mock
    private lateinit var userService: UserService

    @InjectMocks
    private lateinit var orderFacade: OrderFacade

    @DisplayName("상품을 주문하고 결제한다.")
    @Test
    fun order() {
        //given
        val user = UserDomainFixture.create()
        BDDMockito.given(userService.find(any()))
            .willReturn(user)

        BDDMockito.given(orderService.ready(any()))
            .willReturn(1L)

        //when
        val criteria = OrderCriteriaFixture.create(userId = 1L)
        orderFacade.order(criteria)

        //then
        Mockito.verify(userService, times(1))
            .find(any())
        Mockito.verify(orderService, times(1))
            .ready(any())
    }

}