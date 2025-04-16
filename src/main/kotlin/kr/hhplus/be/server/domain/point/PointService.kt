package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.domain.point.PointCommand.Charge
import org.springframework.stereotype.Service

@Service
class PointService(
    private val pointRepository: PointRepository
) {
    fun find(pointId: Long): Point {
        return pointRepository.find(pointId)
    }

    fun charge(command: Charge): Point {
        val point = find(command.pointId)
            .charge(command.amount)

        return pointRepository.save(point)
    }

    fun pay(pointId: Long, amount: Int) {
        val point = pointRepository.find(pointId)
            .deduct(amount)

        pointRepository.save(point)
    }

}