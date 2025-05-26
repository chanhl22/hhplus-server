package kr.hhplus.be.server.domain.platform

interface PlatformSender {

    fun send(platformOrder: PlatformOrder)

}