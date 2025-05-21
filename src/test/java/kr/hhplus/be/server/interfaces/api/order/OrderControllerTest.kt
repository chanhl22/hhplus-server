package kr.hhplus.be.server.interfaces.api.order

import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.application.order.OrderFacade
import kr.hhplus.be.server.fixture.order.OrderResultFixture
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(OrderController::class)
class OrderControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var orderFacade: OrderFacade

    @DisplayName("사용자가 요청한 상품들을 주문하고 결제합니다.")
    @Test
    fun order() {
        //given
        val userId = 1L
        val request = OrderRequest.Order(
            listOf(
                OrderRequest.OrderProduct(1L, 2),
                OrderRequest.OrderProduct(2L, 1)
            ),
            1L
        )

        val result = OrderResultFixture.create()
        BDDMockito.given(orderFacade.order(any()))
            .willReturn(result)

        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/order?userId=${userId}")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
    }
}