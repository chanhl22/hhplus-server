package kr.hhplus.be.server.interfaces.order

import kr.hhplus.be.server.application.order.OrderFacade
import kr.hhplus.be.server.interfaces.swagger.OrderApi
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController(
    private val orderFacade: OrderFacade
) : OrderApi {

    @PostMapping("/order")
    override fun order(
        @RequestParam userId: Long,
        @RequestBody request: OrderRequest.Order
    ): OrderResponse.Order {
        val criteria = request.toCriteria(userId)
        val result = orderFacade.order(criteria)
        return OrderResponse.from(result)
    }

}