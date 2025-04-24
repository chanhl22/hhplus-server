package kr.hhplus.be.server.interfaces.point

import jakarta.validation.constraints.Positive
import kr.hhplus.be.server.application.point.PointCriteria

class PointRequest {
    data class Charge(
        @field:Positive(message = "포인트는 양수여야 합니다.") val amount: Int = 0
    ) {
        fun toCriteria(userId: Long): PointCriteria.Charge {
            return PointCriteria.of(userId, amount)
        }
    }

}