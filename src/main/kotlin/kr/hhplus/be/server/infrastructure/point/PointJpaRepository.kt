package kr.hhplus.be.server.infrastructure.point

import jakarta.persistence.LockModeType
import kr.hhplus.be.server.domain.point.Point
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.util.*

interface PointJpaRepository : JpaRepository<Point, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Point p where p.id = :pointId")
    fun findWithPessimisticLock(pointId: Long): Optional<Point>

}