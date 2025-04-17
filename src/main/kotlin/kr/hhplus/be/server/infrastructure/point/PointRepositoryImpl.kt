package kr.hhplus.be.server.infrastructure.point

import kr.hhplus.be.server.domain.point.Point
import kr.hhplus.be.server.domain.point.PointRepository
import org.springframework.stereotype.Repository

@Repository
class PointRepositoryImpl(
    private val pointJpaRepository: PointJpaRepository
) : PointRepository {

    override fun find(pointId: Long): Point {
        return pointJpaRepository.findById(pointId)
            .orElseThrow(::IllegalArgumentException)
    }

    override fun save(point: Point): Point {
        return pointJpaRepository.save(point)
    }

}