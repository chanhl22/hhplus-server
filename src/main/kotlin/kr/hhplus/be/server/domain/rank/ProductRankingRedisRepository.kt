package kr.hhplus.be.server.domain.rank

interface ProductRankingRedisRepository {

    fun increaseScores(productIdToCount: Map<Long, Int>)

}