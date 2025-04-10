package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.domain.point.PointCommands.PointCommand
import org.springframework.stereotype.Service

@Service
class PointService(
    private val pointRepository: PointRepository
) {
    fun find(pointId: Long): Point {
        return pointRepository.find(pointId)
    }

    fun charge(command: PointCommand): Point {
        val point = pointRepository.find(command.pointId)
        point.charge(command.amount)

        return pointRepository.update(point)
    }

    fun pay(pointId: Long, amount: Int): Point {
        val point = pointRepository.find(pointId)
        point.deduct(amount)

        return pointRepository.update(point)
    }

}