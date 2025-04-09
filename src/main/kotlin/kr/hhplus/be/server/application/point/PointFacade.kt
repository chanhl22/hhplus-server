package kr.hhplus.be.server.application.point

import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.user.UserService
import org.springframework.stereotype.Component

@Component
class PointFacade(
    private val pointService: PointService,
    private val userService: UserService
) {
    fun find(pointCriteria: PointCriteria): PointResult {
        val user = userService.find(pointCriteria.userId)
        val point = pointService.find(user.point.id)
        return PointResult.of(user, point)
    }
}