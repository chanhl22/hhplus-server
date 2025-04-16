package kr.hhplus.be.server.infrastructure.point

import kr.hhplus.be.server.domain.point.Point
import org.springframework.data.jpa.repository.JpaRepository

interface PointJpaRepository : JpaRepository<Point, Long>