package kr.hhplus.be.server.interfaces.user

import jakarta.validation.Valid
import kr.hhplus.be.server.application.point.PointCriteria
import kr.hhplus.be.server.application.point.PointFacade
import kr.hhplus.be.server.point.request.ChargePointRequest
import kr.hhplus.be.server.point.response.ChargePointResponse
import kr.hhplus.be.server.swagger.PointApi
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PointController(
    private val pointFacade: PointFacade
) : PointApi {

    @GetMapping("/point/{id}")
    override fun find(
        @PathVariable id: Long
    ): PointResponses.PointResponse {
        return PointResponses.PointResponse.from(pointFacade.find(PointCriteria.of(id)))
    }

    @PatchMapping("/point/{id}/charge")
    override fun charge(
        @PathVariable id: Long,
        @RequestBody @Valid request: ChargePointRequest,
    ): ChargePointResponse {
        return ChargePointResponse(1L, 10000L, 20000L)
    }

}