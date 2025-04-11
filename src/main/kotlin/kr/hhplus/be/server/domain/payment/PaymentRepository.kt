package kr.hhplus.be.server.domain.payment

import org.springframework.stereotype.Repository

@Repository
interface PaymentRepository {

    fun save(payment: Payment): Payment

}