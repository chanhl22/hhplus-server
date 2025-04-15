package kr.hhplus.be.server.application.point

import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.user.UserService
import org.springframework.stereotype.Component

@Component
class PointFacade(
    private val pointService: PointService,
    private val userService: UserService
) {
    fun find(userId: Long): PointResult.Find {
        val user = userService.find(userId)
        val point = pointService.find(user.pointId)

        return PointResult.of(user, point)
    }

    fun charge(criteria: PointCriteria.Charge): PointResult.Charge {
        val user = userService.find(criteria.userId)

        val command = criteria.toCommand(user.pointId)
        val point = pointService.charge(command)

        return PointResult.of(user, point, criteria.amount)
    }
}