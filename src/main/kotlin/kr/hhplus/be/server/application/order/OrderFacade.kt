package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.common.annotation.DistributedLock
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.user.UserService
import org.springframework.stereotype.Service

@Service
class OrderFacade(
    private val orderService: OrderService,
    private val userService: UserService,
) {

    @DistributedLock(key = "#criteria.toLockKeys()")
    fun order(criteria: OrderCriteria.Order): OrderResult.Order {
        val user = userService.find(criteria.userId)
        val orderId = orderService.ready(criteria.toCommand(user.pointId))
        return OrderResult.of(orderId)
    }

}