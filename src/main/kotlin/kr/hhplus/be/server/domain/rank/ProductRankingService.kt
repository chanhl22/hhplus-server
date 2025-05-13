package kr.hhplus.be.server.domain.rank

import org.springframework.stereotype.Service

@Service
class ProductRankingService(
    private val productRankingRedisRepository: ProductRankingRedisRepository
) {

    fun increaseProductScore(command: ProductRankingCommand.Increase) {
        productRankingRedisRepository.increaseScores(command.orderProductQuantityCountMap)
    }

    fun findProductRanking(limit: Long): List<ProductRanking> {
        return productRankingRedisRepository.findDailyTop(limit)
    }

}