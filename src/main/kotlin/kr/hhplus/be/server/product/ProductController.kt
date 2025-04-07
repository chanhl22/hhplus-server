package kr.hhplus.be.server.product

import kr.hhplus.be.server.product.response.ProductResponse
import kr.hhplus.be.server.product.response.TopSellingProductResponse
import kr.hhplus.be.server.swagger.ProductApi
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController : ProductApi {

    @GetMapping("/product/{id}")
    override fun find(
        @PathVariable id: Long,
    ): ProductResponse {
        return ProductResponse(1L, "무선 블루투스 이어폰", 129000, 25, "고음질 무선 블루투스 이어폰. 최대 20시간 사용 가능.")
    }

    @GetMapping("/products/top")
    override fun findTopSellingProducts(): List<TopSellingProductResponse> {
        return listOf(
            TopSellingProductResponse(1, 10L, "무선 블루투스 이어폰", 129000, 350, 25),
            TopSellingProductResponse(2, 5L, "게이밍 키보드", 89000, 280, 50),
        )
    }

}