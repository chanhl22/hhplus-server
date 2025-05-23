package kr.hhplus.be.server.application.point

import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.user.UserService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class PointFacade(
    private val pointService: PointService,
    private val userService: UserService
) {
    fun find(userId: Long): PointResult.Find {
        val user = userService.find(userId)
        val point = pointService.find(user.pointId)

        return PointResult.of(user, point)
    }

    @Transactional
    fun charge(criteria: PointCriteria.Charge): PointResult.Charge {
        val user = userService.find(criteria.userId)
        val point = pointService.find(user.pointId)

        val chargedPoint = pointService.charge(point.id, criteria.amount)

        return PointResult.of(user, chargedPoint, criteria.amount)
    }
}