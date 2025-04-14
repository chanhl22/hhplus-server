package kr.hhplus.be.server.domain.point

import org.springframework.stereotype.Repository

@Repository
interface PointRepository {

    fun find(pointId: Long): Point

    fun update(point: Point): Point

}