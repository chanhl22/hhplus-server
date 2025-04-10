package kr.hhplus.be.server.interfaces.user

import jakarta.validation.Valid
import kr.hhplus.be.server.application.point.PointFacade
import kr.hhplus.be.server.interfaces.swagger.PointApi
import kr.hhplus.be.server.interfaces.user.PointRequests.ChargePointRequest
import kr.hhplus.be.server.interfaces.user.PointResponses.ChargePointResponse
import kr.hhplus.be.server.interfaces.user.PointResponses.PointResponse
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
    ): PointResponse {
        val result = pointFacade.find(id)
        return PointResponse.from(result)
    }

    @PatchMapping("/point/{id}/charge")
    override fun charge(
        @PathVariable id: Long,
        @RequestBody @Valid request: ChargePointRequest
    ): ChargePointResponse {
        val result = pointFacade.charge(request.toCriterion(id))
        return ChargePointResponse.of(result, request.amount)
    }

}