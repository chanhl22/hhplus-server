package kr.hhplus.be.server.infrastructure.point

import kr.hhplus.be.server.domain.point.PointEvent
import kr.hhplus.be.server.domain.point.PointEventPublisher
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class PointSpringEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher
) : PointEventPublisher {

    override fun publish(event: PointEvent.Completed) {
        applicationEventPublisher.publishEvent(event)
    }

}