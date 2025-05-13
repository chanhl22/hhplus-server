package kr.hhplus.be.server.domain.rank

interface ProductRankingRedisRepository {

    fun increaseScores(productIdToCount: Map<Long, Int>)

    fun findDailyTop(limit: Long): List<ProductRanking>

}