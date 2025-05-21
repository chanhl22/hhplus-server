package kr.hhplus.be.server.domain.rank

import java.time.LocalDate

interface ProductRankingRepository {

    fun increaseDailyRanking(now: LocalDate, productId: Long, name: String, quantity: Int)

    fun increaseWeeklyRanking(now: LocalDate, productId: Long, name: String, quantity: Int)

    fun findDailyTopRank(now: LocalDate, limit: Long): List<Pair<String, Int>>

    fun findWeeklyTopRank(now: LocalDate, limit: Long): List<Pair<String, Int>>

    fun findDailyProductNames(now: LocalDate, productIds: List<String>): Map<String, String>

    fun findWeeklyProductNames(now: LocalDate, productIds: List<String>): Map<String, String>

}