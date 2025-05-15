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

    fun findProductDailyRanking(limit: Long): List<ProductRanking> {
        val now = LocalDate.now()
        val dailyRanking = productRankingRepository.findDailyTopRank(now, limit)

        val productIds = dailyRanking.map { it.first }
        val productNameMap = productRankingRepository.findDailyProductNames(now, productIds)

        return dailyRanking.map { ProductRanking.of(it.first, productNameMap[it.first], it.second) }
    }

    fun findProductWeeklyRanking(limit: Long): List<ProductRanking> {
        val now = LocalDate.now()
        val dailyRanking = productRankingRepository.findWeeklyTopRank(now, limit)

        val productIds = dailyRanking.map { it.first }
        val productNameMap = productRankingRepository.findWeeklyProductNames(now, productIds)

        return dailyRanking.map { ProductRanking.of(it.first, productNameMap[it.first], it.second) }
    }

}