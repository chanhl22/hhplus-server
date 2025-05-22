package kr.hhplus.be.server.application.order

class OrderResult {
    data class Order(
        val orderId: Long
    )

    companion object {
        fun of(orderId: Long): Order {
            return Order(
                orderId = orderId
            )
        }
    }

}
