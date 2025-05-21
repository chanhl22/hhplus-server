package kr.hhplus.be.server.domain.platform

import org.springframework.stereotype.Service

@Service
class PlatformSendService(
    private val platformSender: PlatformSender
) {

    fun send(command: PlatformCommand.Payload) {
        val platformOrder = PlatformOrderMapper.toDomain(command)
        platformSender.send(platformOrder)
    }

}