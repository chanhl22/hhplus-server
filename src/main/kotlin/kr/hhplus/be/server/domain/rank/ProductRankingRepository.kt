package kr.hhplus.be.server.domain.rank

import java.time.LocalDate

interface ProductRankingRepository {

    fun increaseDailyRanking(now: LocalDate, productId: Long, name: String, quantity: Int)

    fun increaseWeeklyRanking(now: LocalDate, productId: Long, name: String, quantity: Int)

    fun findDailyTop(limit: Long): List<ProductRanking>

}