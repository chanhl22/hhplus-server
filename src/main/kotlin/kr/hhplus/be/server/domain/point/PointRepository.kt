package kr.hhplus.be.server.domain.point

interface PointRepository {

    fun find(pointId: Long): Point

    fun findWithPessimisticLock(pointId: Long): Point

    fun save(point: Point): Point

}