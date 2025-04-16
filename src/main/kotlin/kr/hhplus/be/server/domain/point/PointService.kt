package kr.hhplus.be.server.domain.point

import org.springframework.stereotype.Service

@Service
class PointService(
    private val pointRepository: PointRepository
) {
    fun find(pointId: Long): Point {
        return pointRepository.find(pointId)
    }

    fun charge(pointId: Long, amount: Int): Point {
        val point = find(pointId)
            .charge(amount)

        return pointRepository.save(point)
    }

    fun use(pointId: Long, amount: Int) {
        val point = find(pointId)
            .deduct(amount)

        pointRepository.save(point)
    }

}