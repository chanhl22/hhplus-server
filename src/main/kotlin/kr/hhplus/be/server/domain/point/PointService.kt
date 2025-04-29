package kr.hhplus.be.server.domain.point

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class PointService(
    private val pointRepository: PointRepository
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
    fun use(pointId: Long, amount: Int) {
        val point = pointRepository.findWithPessimisticLock(pointId)
            .deduct(amount)

        pointRepository.save(point)
    }

}