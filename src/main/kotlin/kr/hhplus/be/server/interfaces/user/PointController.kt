package kr.hhplus.be.server.interfaces.user

import jakarta.validation.Valid
import kr.hhplus.be.server.application.point.PointFacade
import kr.hhplus.be.server.interfaces.swagger.PointApi
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
        val result = pointFacade.find(id)
        return PointResponses.PointResponse.from(result)
    }

    @PatchMapping("/point/{id}/charge")
    override fun charge(
        @PathVariable id: Long,
        @RequestBody @Valid request: PointRequests.ChargePointRequest
    ): PointResponses.ChargePointResponse {
        val result = pointFacade.charge(request.toCriterion(id))
        return PointResponses.ChargePointResponse.of(result, request.amount)
    }

}