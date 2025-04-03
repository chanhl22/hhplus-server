package kr.hhplus.be.server.coupon

import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.coupon.request.FirstComeCouponRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(CouponController::class)
class CouponControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @DisplayName("선착순으로 쿠폰을 발급합니다.")
    @Test
    fun issueCouponFirstCome() {
        //given
        val request = FirstComeCouponRequest(1L, 1L)

        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/coupon/issue")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @DisplayName("발급된 쿠폰 목록을 조회합니다.")
    @Test
    fun findCoupons() {
        //given
        val requestId = 1L

        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/coupons/${requestId}")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

}