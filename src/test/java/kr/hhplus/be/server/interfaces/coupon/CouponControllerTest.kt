package kr.hhplus.be.server.interfaces.coupon

import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.fixture.coupon.CouponRequestFixture
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

@WebMvcTest(CouponController::class)
class CouponControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var couponService: CouponService

    @DisplayName("선착순으로 쿠폰 발급을 요청한다.")
    @Test
    fun reserveFirstCome() {
        //given
        val request = CouponRequestFixture.create()

        BDDMockito.willDoNothing()
            .given(couponService)
            .reserveFirstCome(any(), any())

        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/coupon/reserve")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

}