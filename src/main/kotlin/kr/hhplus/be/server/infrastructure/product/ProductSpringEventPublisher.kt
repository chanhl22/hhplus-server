package kr.hhplus.be.server.infrastructure.product

import kr.hhplus.be.server.domain.product.ProductEvent
import kr.hhplus.be.server.domain.product.ProductEventPublisher
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class ProductSpringEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher
) : ProductEventPublisher {

    override fun publish(event: ProductEvent.Completed) {
        applicationEventPublisher.publishEvent(event)
    }

}