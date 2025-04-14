package kr.hhplus.be.server.interfaces.user

import jakarta.validation.constraints.Positive
import kr.hhplus.be.server.application.point.PointCriteria

class PointRequests {
    data class ChargePointRequest(
        @field:Positive(message = "포인트는 양수여야 합니다.") val amount: Int = 0
    ) {
        fun toCriterion(userId: Long): PointCriteria.ChargePointCriterion {
            return PointCriteria.ChargePointCriterion.of(userId, amount)
        }
    }
}