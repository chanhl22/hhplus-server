package kr.hhplus.be.server.domain.rank

import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ProductRankingService(
    private val productRankingRepository: ProductRankingRepository
) {

    fun upsertRanking(command: ProductRankingCommand.Increase) {
        val now = LocalDate.now()
        command.productIdToNameAndQuantityMap.forEach { (productId, nameAndQuantity) ->
            val (name, quantity) = nameAndQuantity
            productRankingRepository.increaseDailyRanking(now, productId, name, quantity)
            productRankingRepository.increaseWeeklyRanking(now, productId, name, quantity)
        }
    }

    fun findProductRanking(limit: Long): List<ProductRanking> {
        return productRankingRepository.findDailyTop(limit)
    }

}