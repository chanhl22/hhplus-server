package kr.hhplus.be.server.domain.user

import kr.hhplus.be.server.domain.point.Point

class User(
    val id: Long,
    val name: String,
    val point: Point
)
