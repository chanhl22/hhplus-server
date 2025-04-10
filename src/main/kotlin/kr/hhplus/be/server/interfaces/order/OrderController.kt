package kr.hhplus.be.server.interfaces.order

import kr.hhplus.be.server.application.order.OrderFacade
import kr.hhplus.be.server.interfaces.order.OrderRequests.OrderRequest
import kr.hhplus.be.server.interfaces.order.OrderResponses.OrderResponse
import kr.hhplus.be.server.swagger.OrderApi
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController(
    private val orderFacade: OrderFacade
) : OrderApi {

    @PostMapping("/order")
    override fun order(@RequestBody request: OrderRequest): OrderResponse {
        val result = orderFacade.order(request.toCriterion())
        return OrderResponse.of(result)
    }

}