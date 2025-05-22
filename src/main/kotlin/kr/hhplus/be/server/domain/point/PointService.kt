package kr.hhplus.be.server.domain.point

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class PointService(
    private val pointRepository: PointRepository,
    private val pointEventPublisher: PointEventPublisher
) {
    fun find(pointId: Long): Point {
        return pointRepository.find(pointId)
    }

    @Transactional
    fun charge(pointId: Long, amount: Int): Point {
        val point = pointRepository.findWithPessimisticLock(pointId)
            .charge(amount)

        return pointRepository.save(point)
    }

    @Transactional
    fun use(command: PointCommand.Deduct) {
        val point = pointRepository.findWithPessimisticLock(command.pointId)
            .deduct(command.totalPrice)

        pointRepository.save(point)

        pointEventPublisher.publish(command.toEvent())
    }

}