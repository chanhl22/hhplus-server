package kr.hhplus.be.server.domain.platform

import kr.hhplus.be.server.fixture.platform.PlatformCommandFixture
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
class PlatformSendServiceTest {

    @Mock
    private lateinit var platformSender: PlatformSender

    @InjectMocks
    private lateinit var platformSendService: PlatformSendService

    @DisplayName("데이터 플랫폼에 전송한다.")
    @Test
    fun publish() {
        //given
        val command = PlatformCommandFixture.create()
        BDDMockito.willDoNothing()
            .given(platformSender)
            .send(any())

        //when
        platformSendService.send(command)

        //then
        Mockito.verify(platformSender, times(1))
            .send(any())
    }

}