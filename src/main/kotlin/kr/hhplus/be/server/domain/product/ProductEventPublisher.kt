package kr.hhplus.be.server.domain.product

interface ProductEventPublisher {

    fun publish(event: ProductEvent.Completed)

}