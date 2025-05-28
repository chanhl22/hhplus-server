package kr.hhplus.be.server.interfaces.event.platform

import kr.hhplus.be.server.domain.platform.PlatformSendService
import kr.hhplus.be.server.fixture.order.OrderEventFixture
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any

@ExtendWith(MockitoExtension::class)
class PlatformExternalEventListenerTest {

    @Mock
    private lateinit var platformSendService: PlatformSendService

    @InjectMocks
    private lateinit var platformExternalEventListener: PlatformExternalEventListener

    @DisplayName("데이터 플랫폼에 전송한다.")
    @Test
    fun publish() {
        //given
        val event = OrderEventFixture.create()
        BDDMockito.willDoNothing()
            .given(platformSendService)
            .send(any())

        //when
        platformExternalEventListener.handle(event)

        //then
        Mockito.verify(platformSendService, times(1))
            .send(any())
    }

}