package kr.hhplus.be.server.interfaces.product

import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.interfaces.product.ProductResponses.ProductResponse
import kr.hhplus.be.server.interfaces.product.ProductResponses.TopSellingProductResponse
import kr.hhplus.be.server.interfaces.swagger.ProductApi
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
    ): ProductResponse {
        val domain = productService.find(id)
        return ProductResponse.from(domain)
    }

    @GetMapping("/products/top")
    override fun findTopSellingProducts(): List<TopSellingProductResponse> {
        val domain = productService.findTopSellingProducts()
        return domain.map { product -> TopSellingProductResponse.from(product) }
    }

}