package kr.hhplus.be.server.interfaces.point

import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.application.point.PointFacade
import kr.hhplus.be.server.fixture.point.ChargePointResultFixture
import kr.hhplus.be.server.fixture.point.PointResultFixture
import kr.hhplus.be.server.interfaces.user.PointController
import kr.hhplus.be.server.interfaces.user.PointRequests
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito
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
        val requestId = 1L

        val fakeResult = PointResultFixture.create()
        BDDMockito.given(pointFacade.find(ArgumentMatchers.anyLong()))
            .willReturn(fakeResult)

        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/point/${requestId}")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @DisplayName("충전할 금액을 받아 잔액을 충전합니다.")
    @Test
    fun charge() {
        //given
        val requestId = 1L
        val request = PointRequests.ChargePointRequest(10000)

        val fakeResult = ChargePointResultFixture.create()
        BDDMockito.given(pointFacade.charge(request.toCriterion(requestId)))
            .willReturn(fakeResult)

        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/point/${requestId}/charge")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

}