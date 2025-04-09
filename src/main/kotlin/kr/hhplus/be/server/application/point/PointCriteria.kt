package kr.hhplus.be.server.application.point

data class PointCriteria (
    val userId: Long
) {
    companion object {
        fun of(userId: Long): PointCriteria {
            return PointCriteria(
                userId = userId
            )
        }
    }
}
