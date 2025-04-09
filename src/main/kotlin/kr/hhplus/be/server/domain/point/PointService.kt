package kr.hhplus.be.server.domain.point

import org.springframework.stereotype.Service

@Service
class PointService(
    private val pointRepository: PointRepository
) {
    fun find(pointId: Long): Point {
        return pointRepository.find(pointId)
    }

    fun charge(command: PointCommands.PointCommand): Point {
        val point = pointRepository.find(command.pointId)
        point.charge(command.amount)

        return pointRepository.update(point)
    }
}