package kr.hhplus.be.server.domain.coupon

interface CouponEventPublisher {

    fun publish(event: CouponEvent.Completed)

    fun publish(event: CouponEvent.Created)

}