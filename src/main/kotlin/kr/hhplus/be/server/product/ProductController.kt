package kr.hhplus.be.server.product

import kr.hhplus.be.server.product.response.ProductResponse
import kr.hhplus.be.server.product.response.TopProductResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController {

    @GetMapping("/product/{id}")
    fun findProduct(
        @PathVariable id: Long,
    ): ProductResponse {
        return ProductResponse(1L, "상품 A", 10000L, 10)
    }

    @GetMapping("/product/top")
    fun findTopSellingProducts(): List<TopProductResponse> {
        return listOf(
            TopProductResponse(1L, "노트북", 1500000, 10),
            TopProductResponse(1L, "스마트폰", 800000, 5),
            TopProductResponse(1L, "무선 이어폰", 200000, 1)
        )
    }

}