package kr.hhplus.be.server.product

import kr.hhplus.be.server.product.response.ProductResponse
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

}