package kr.hhplus.be.server.interfaces.rank

import kr.hhplus.be.server.domain.rank.ProductRankingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductRankingController(
    private val productRankingService: ProductRankingService
) {

    @GetMapping("/product/rank/daily")
    fun find(
        @RequestParam limit: Long
    ): List<ProductRankingResponse.Daily> {
        val domain = productRankingService.findProductRanking(limit)
        return domain.map { ProductRankingResponse.from(it) }
    }

}
