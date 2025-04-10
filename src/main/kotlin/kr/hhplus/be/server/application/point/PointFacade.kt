package kr.hhplus.be.server.application.point

import kr.hhplus.be.server.application.point.PointCriteria.ChargePointCriterion
import kr.hhplus.be.server.application.point.PointResults.ChargePointResult
import kr.hhplus.be.server.application.point.PointResults.PointResult
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.user.UserService
import org.springframework.stereotype.Component

@Component
class PointFacade(
    private val pointService: PointService,
    private val userService: UserService
) {
    fun find(userId: Long): PointResult {
        val user = userService.find(userId)
        val point = pointService.find(user.point.id)
        return PointResult.of(user, point)
    }

    fun charge(criterion: ChargePointCriterion): ChargePointResult {
        val user = userService.findUserWithPoint(criterion.userId)
        val point = pointService.charge(criterion.toCommand(user.point.id))
        return ChargePointResult.of(user, point)
    }
}