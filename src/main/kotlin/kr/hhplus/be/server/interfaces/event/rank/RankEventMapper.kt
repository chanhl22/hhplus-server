package kr.hhplus.be.server.interfaces.event.rank

import kr.hhplus.be.server.domain.order.OrderEvent
import kr.hhplus.be.server.domain.rank.ProductRankingCommand

class RankEventMapper {
    companion object {
        fun toCommand(event: OrderEvent.Completed): ProductRankingCommand.Increase {
            return ProductRankingCommand.of(
                productIdToNameAndQuantityMap = event.createProductIdToNameAndQuantityMap()
            )
        }
    }

}