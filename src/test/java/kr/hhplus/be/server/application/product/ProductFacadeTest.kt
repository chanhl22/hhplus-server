package kr.hhplus.be.server.application.product

import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.fixture.order.OrderInfoFixture
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
class ProductFacadeTest {

    @Mock
    private lateinit var productService: ProductService

    @Mock
    private lateinit var orderService: OrderService

    @InjectMocks
    private lateinit var productFacade: ProductFacade

    @DisplayName("주문의 상품 통계를 저장한다.")
    @Test
    fun scheduledProductStatistics() {
        //given
        val orderInfo = listOf(OrderInfoFixture.create())
        BDDMockito.given(orderService.aggregateOrderProduct())
            .willReturn(orderInfo)

        BDDMockito.willDoNothing()
            .given(productService)
            .saveProductStatistics(any())

        //when
        productFacade.scheduledProductStatistics()

        //then
        Mockito.verify(orderService, times(1))
            .aggregateOrderProduct()
        Mockito.verify(productService, times(1))
            .saveProductStatistics(any())
    }

}