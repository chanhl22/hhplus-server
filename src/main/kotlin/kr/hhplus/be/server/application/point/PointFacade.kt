package kr.hhplus.be.server.application.point

import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.user.UserService
import org.springframework.stereotype.Component

@Component
class PointFacade(
    private val pointService: PointService,
    private val userService: UserService
) {
    fun find(userId: Long): PointResults.PointResult {
        val user = userService.find(userId)
        val point = pointService.find(user.point.id)
        return PointResults.PointResult.of(user, point)
    }

    fun charge(criterion: PointCriteria.ChargePointCriterion): PointResults.ChargePointResult {
        val user = userService.find(criterion.userId)
        val point = pointService.charge(criterion.toCommand())
        return PointResults.ChargePointResult.of(user, point)
    }
}