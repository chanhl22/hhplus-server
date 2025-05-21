package kr.hhplus.be.server.infrastructure.platform

import kr.hhplus.be.server.domain.platform.PlatformOrder
import kr.hhplus.be.server.domain.platform.PlatformSender
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class PlatformSenderImpl : PlatformSender {

    private val log: Logger = LoggerFactory.getLogger(PlatformSenderImpl::class.java)

    override fun send(platformOrder: PlatformOrder) {
        log.info("플랫폼 주문 데이터 전송 시작")
        Thread.sleep(2000)
    }

}