package kr.hhplus.be.server.fixture.rank

import kr.hhplus.be.server.domain.rank.ProductRanking
import kr.hhplus.be.server.domain.rank.ProductRankingCommand

object ProductRankingCommandFixture {
    fun create(
        productIdToNameAndQuantityMap: Map<Long, Pair<String, Int>> = mapOf(
            1L to ("상품1" to 3),
            2L to ("상품2" to 5),
            3L to ("상품3" to 2)
        )
    ): ProductRankingCommand.Increase {
        return ProductRankingCommand.Increase(
            productIdToNameAndQuantityMap = productIdToNameAndQuantityMap
        )
    }
}

object ProductRankingDomainFixture {
    fun createDaily(
        productId: Long = 1L,
        rank: Int = 1
    ): ProductRanking {
        return ProductRanking(
            productId = productId,
            rank = rank
        )
    }
}