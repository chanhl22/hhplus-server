package kr.hhplus.be.server.point.response

data class ChargePointResponse(
    val userId: Long,
    val chargedAmount: Long,
    val balance: Long
)
