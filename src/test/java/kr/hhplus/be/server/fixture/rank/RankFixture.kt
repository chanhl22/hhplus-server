package kr.hhplus.be.server.fixture.rank

import kr.hhplus.be.server.domain.rank.ProductRanking

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