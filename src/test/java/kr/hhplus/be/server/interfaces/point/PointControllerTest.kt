package kr.hhplus.be.server.interfaces.point

import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.application.point.PointFacade
import kr.hhplus.be.server.fixture.point.PointResultFixture
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

@WebMvcTest(PointController::class)
class PointControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var pointFacade: PointFacade

    @DisplayName("사용자의 잔액을 조회합니다.")
    @Test
    fun find() {
        //given
        val userId = 1L

        val result = PointResultFixture.createFind()
        BDDMockito.given(pointFacade.find(any()))
            .willReturn(result)

        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/point?userId=${userId}")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @DisplayName("충전할 금액을 받아 잔액을 충전합니다.")
    @Test
    fun charge() {
        //given
        val userId = 1L
        val request = PointRequest.Charge(10000)

        val result = PointResultFixture.createCharge()
        BDDMockito.given(pointFacade.charge(any()))
            .willReturn(result)

        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/point/charge?userId=${userId}")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

}