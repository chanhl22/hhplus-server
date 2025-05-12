package kr.hhplus.be.server.domain.rank

class ProductRankingCommand {
    data class Increase(
        val orderProductQuantityCountMap: Map<Long, Int>
    )

    companion object {
        fun of(orderProductQuantityCountMap: Map<Long, Int>): Increase {
            return Increase(orderProductQuantityCountMap = orderProductQuantityCountMap)
        }
    }

}