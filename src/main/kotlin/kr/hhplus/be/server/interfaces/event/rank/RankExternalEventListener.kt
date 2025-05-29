package kr.hhplus.be.server.interfaces.event.rank

import kr.hhplus.be.server.domain.order.OrderEvent
import kr.hhplus.be.server.domain.rank.ProductRankingService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class RankExternalEventListener(
    private val productRankingService: ProductRankingService
) {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handle(event: OrderEvent.Completed) {
        val command = RankExternalEventMapper.toCommand(event)
        productRankingService.upsertRanking(command)
    }

}