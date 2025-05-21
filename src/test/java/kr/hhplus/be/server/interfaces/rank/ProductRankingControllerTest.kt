package kr.hhplus.be.server.interfaces.rank

import kr.hhplus.be.server.domain.rank.ProductRankingService
import kr.hhplus.be.server.fixture.rank.ProductRankingDomainFixture
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(ProductRankingController::class)
class ProductRankingControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var productRankingService: ProductRankingService

    @DisplayName("상품의 일간 랭킹을 조회한다.")
    @Test
    fun findDaily() {
        //given
        val limit = 2L

        val daily1 = ProductRankingDomainFixture.createDaily(productId = 1L, rank = 1)
        val daily2 = ProductRankingDomainFixture.createDaily(productId = 2L, rank = 2)
        BDDMockito.given(productRankingService.findProductDailyRanking(any()))
            .willReturn(listOf(daily1, daily2))

        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/product/rank/daily?limit=${limit}")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @DisplayName("상품의 주간 랭킹을 조회한다.")
    @Test
    fun findWeekly() {
        //given
        val limit = 2L

        val daily1 = ProductRankingDomainFixture.createWeekly(productId = 1L, rank = 1)
        val daily2 = ProductRankingDomainFixture.createWeekly(productId = 2L, rank = 2)
        BDDMockito.given(productRankingService.findProductWeeklyRanking(any()))
            .willReturn(listOf(daily1, daily2))

        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/product/rank/weekly?limit=${limit}")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

}