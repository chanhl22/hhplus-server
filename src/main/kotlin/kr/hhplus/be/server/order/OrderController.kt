package kr.hhplus.be.server.order

import kr.hhplus.be.server.order.request.OrderRequest
import kr.hhplus.be.server.order.response.OrderResponse
import kr.hhplus.be.server.swagger.OrderApi
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController : OrderApi {

    @PostMapping("/order")
    override fun placeOrder(@RequestBody request: OrderRequest): OrderResponse {
        return OrderResponse(1L, 10000, "SUCCESS")
    }

}