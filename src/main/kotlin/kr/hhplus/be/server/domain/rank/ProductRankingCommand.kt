package kr.hhplus.be.server.domain.rank

class ProductRankingCommand {
    data class Increase(
        val productIdToNameAndQuantityMap: Map<Long, Pair<String, Int>>
    )

    companion object {
        fun of(productIdToNameAndQuantityMap: Map<Long, Pair<String, Int>>): Increase {
            return Increase(productIdToNameAndQuantityMap = productIdToNameAndQuantityMap)
        }
    }

}