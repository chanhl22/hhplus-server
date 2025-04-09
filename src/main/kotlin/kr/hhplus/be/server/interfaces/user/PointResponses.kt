package kr.hhplus.be.server.interfaces.user

import kr.hhplus.be.server.application.point.PointResult

class PointResponses {
    data class PointResponse(
        val userId: Long,
        val balance: Int
    ) {
        companion object {
            fun from(pointResult: PointResult): PointResponse {
                return PointResponse(
                    userId = pointResult.user.id,
                    balance = pointResult.point.amount
                )
            }
        }
    }
}