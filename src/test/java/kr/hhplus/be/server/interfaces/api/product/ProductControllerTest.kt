package kr.hhplus.be.server.interfaces.api.product

import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.fixture.product.ProductInfoFixture
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

@WebMvcTest(ProductController::class)
class ProductControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var productService: ProductService

    @DisplayName("상품 정보를 조회합니다.")
    @Test
    fun find() {
        //given
        val requestId = 1L

        val result = ProductInfoFixture.create()
        BDDMockito.given(productService.find(any()))
            .willReturn(result)

        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/product/${requestId}")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @DisplayName("최근 3일간 가장 많이 팔린 상위 5개 상품 정보를 조회합니다.")
    @Test
    fun findTopSellingProducts() {
        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/products/top")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

}