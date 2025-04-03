package kr.hhplus.be.server.point.request

import jakarta.validation.constraints.Positive

data class UserPointRequest(
    @field:Positive(message = "포인트는 양수여야 합니다.") val amount: Long = 0L
)
