package kr.hhplus.be.server.interfaces.coupon

import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.application.coupon.CouponFacade
import kr.hhplus.be.server.fixture.coupon.CouponCriterionFixture
import kr.hhplus.be.server.fixture.coupon.CouponResultFixture
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
    private lateinit var couponFacade: CouponFacade

    @DisplayName("선착순으로 쿠폰을 발급합니다.")
    @Test
    fun issueCouponFirstCome() {
        //given
        val request = CouponCriterionFixture.create()

        val result = CouponResultFixture.create()
        BDDMockito.given(couponFacade.issueCouponFirstCome(any()))
            .willReturn(result)

        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/coupon/issue")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

}