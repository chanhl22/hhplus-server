package kr.hhplus.be.server.interfaces.product

import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.interfaces.product.response.TopSellingProductResponse
import kr.hhplus.be.server.swagger.ProductApi
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(
    private val productService: ProductService
) : ProductApi {

    @GetMapping("/product/{id}")
    override fun find(
        @PathVariable id: Long
    ): ProductResponse.FoundProduct {
        val domain = productService.find(id)
        return ProductResponse.FoundProduct.from(domain)
    }

    @GetMapping("/products/top")
    override fun findTopSellingProducts(): List<TopSellingProductResponse> {
        return listOf(
            TopSellingProductResponse(1, 10L, "무선 블루투스 이어폰", 129000, 350, 25),
            TopSellingProductResponse(2, 5L, "게이밍 키보드", 89000, 280, 50),
        )
    }

}