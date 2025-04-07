package kr.hhplus.be.server.swagger

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import kr.hhplus.be.server.order.request.OrderRequest
import kr.hhplus.be.server.order.response.OrderResponse

@Tag(name = "Order API", description = "주문 API")
interface OrderApi {

    @Operation(summary = "주문 / 결제", description = "사용자가 요청한 상품들을 주문하고 결제합니다.")
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
                            "orderId": 1L,
                            "finalAmount": 10000,
                            "status": "SUCCESS"
                        }"""
                        )
                    )
                ]
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
    fun placeOrder(
        @RequestBody(
            description = "주문 생성 정보",
            required = true,
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(
                        implementation = OrderRequest::class,
                        example = """{
                                        "userId": 1,
                                        "items": [
                                            {
                                              "productId": 1,
                                              "quantity": 2
                                            },
                                            {
                                              "productId": 2,
                                              "quantity": 1
                                            }
                                        ]
                                     }"""
                    )
                )
            ]
        )
        request: OrderRequest
    ): OrderResponse

}