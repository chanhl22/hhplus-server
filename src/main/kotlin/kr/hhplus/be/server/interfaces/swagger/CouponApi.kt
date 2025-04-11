package kr.hhplus.be.server.interfaces.swagger

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import kr.hhplus.be.server.interfaces.coupon.request.FirstComeCouponRequest
import kr.hhplus.be.server.interfaces.coupon.response.CouponsResponse
import kr.hhplus.be.server.interfaces.coupon.response.FirstComeCouponResponse

@Tag(name = "Coupon API", description = "쿠폰 API")
interface CouponApi {

    @Operation(summary = "선착순 쿠폰 발급", description = "선착순으로 쿠폰을 발급합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "성공적으로 데이터를 반환합니다.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            example = """{
                                            "success": true,
                                            "couponId": 1001,
                                            "name": "10% 할인 쿠폰",
                                            "discountType": "PERCENTAGE",
                                            "discountValue": 10,
                                            "expiresAt": "2024-04-10T23:59:59Z",
                                            "issuedAt": "2024-04-03T12:00:00Z"
                                         }"""
                        )
                    )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "해당 정보가 존재하지 않습니다.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            example = """{
                                            "code": "400",
                                            "message": "잘못된 요청입니다."
                                         }"""
                        )
                    )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "알 수 없는 에러가 발생했습니다.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            example = """{
                                            "code": "500",
                                            "message": "알 수 없는 에러가 발생했습니다."
                                         }"""
                        )
                    )]
            )
        ]
    )
    fun issueCouponFirstCome(
        @RequestBody(
            description = "사용자 ID와 발급할 쿠폰 ID 정보",
            required = true,
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(
                        implementation = FirstComeCouponRequest::class,
                        example = """{
                            "userId": 1,
                            "couponId": 1
                        }"""
                    )
                )
            ]
        ) request: FirstComeCouponRequest
    ): FirstComeCouponResponse

    @Operation(summary = "쿠폰 목록 조회", description = "발급된 쿠폰 목록을 조회합니다.")
    @Parameter(name = "id", description = "사용자 ID", example = "1")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "성공적으로 데이터를 반환합니다.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            example = """[
                                            {
                                              "couponId": 1001,
                                              "name": "10% 할인 쿠폰",
                                              "discountType": "PERCENTAGE",
                                              "discountValue": 10,
                                              "expiresAt": "2024-04-10T23:59:59Z",
                                              "issuedAt": "2024-04-03T12:00:00Z",
                                              "isUsed": false
                                            },
                                            {
                                              "couponId": 1002,
                                              "name": "5,000원 할인 쿠폰",
                                              "discountType": "FIXED_AMOUNT",
                                              "discountValue": 5000,
                                              "expiresAt": "2024-04-15T23:59:59Z",
                                              "issuedAt": "2024-04-01T15:30:00Z",
                                              "isUsed": true
                                            }
                                         ]"""
                        )
                    )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "해당 정보가 존재하지 않습니다.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            example = """{
                                            "code": "400",
                                            "message": "잘못된 요청입니다."
                                         }"""
                        )
                    )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "알 수 없는 에러가 발생했습니다.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            example = """{
                                            "code": "500",
                                            "message": "알 수 없는 에러가 발생했습니다."
                                         }"""
                        )
                    )]
            )
        ]
    )
    fun findCoupons(id: Long): List<CouponsResponse>

}