package kr.hhplus.be.server.interfaces.api.rank

import kr.hhplus.be.server.domain.rank.ProductRankingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductRankingController(
    private val productRankingService: ProductRankingService
) {

    @GetMapping("/product/rank/daily")
    fun findDaily(
        @RequestParam limit: Long
    ): List<ProductRankingResponse.Daily> {
        val domain = productRankingService.findProductDailyRanking(limit)
        return domain.map { ProductRankingResponse.fromDaily(it) }
    }

    @GetMapping("/product/rank/weekly")
    fun findWeekly(
        @RequestParam limit: Long
    ): List<ProductRankingResponse.Weekly> {
        val domain = productRankingService.findProductWeeklyRanking(limit)
        return domain.map { ProductRankingResponse.fromWeekly(it) }
    }

}
