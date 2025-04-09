package kr.hhplus.be.server.interfaces.order

import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.order.OrderController
import kr.hhplus.be.server.order.request.OrderProductRequest
import kr.hhplus.be.server.order.request.OrderRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
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

    @DisplayName("사용자가 요청한 상품들을 주문하고 결제합니다.")
    @Test
    fun placeOrder() {
        //given
        val request = OrderRequest(
            1L,
            listOf(
                OrderProductRequest(1, 2),
                OrderProductRequest(2, 1)
            )
        )

        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/order")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
    }
}