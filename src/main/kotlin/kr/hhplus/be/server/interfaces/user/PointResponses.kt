package kr.hhplus.be.server.interfaces.user

import kr.hhplus.be.server.application.point.PointResults

class PointResponses {
    data class PointResponse(
        val userId: Long,
        val balance: Int
    ) {
        companion object {
            fun from(pointResult: PointResults.PointResult): PointResponse {
                return PointResponse(
                    userId = pointResult.user.id,
                    balance = pointResult.point.balance
                )
            }
        }
    }

    data class ChargePointResponse(
        val userId: Long,
        val chargedAmount: Int,
        val balance: Int
    ) {
        companion object {
            fun of(result: PointResults.ChargePointResult, amount: Int): ChargePointResponse {
                return ChargePointResponse(
                    userId = result.user.id,
                    chargedAmount = amount,
                    balance = result.point.balance
                )
            }
        }
    }
}