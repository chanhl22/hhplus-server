package kr.hhplus.be.server.domain.point

interface PointEventPublisher {

    fun publish(event: PointEvent.Completed)

}