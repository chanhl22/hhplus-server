package kr.hhplus.be.server.infrastructure.order

import kr.hhplus.be.server.domain.order.OrderEvent
import kr.hhplus.be.server.fixture.order.OrderEventFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.event.ApplicationEvents
import org.springframework.test.context.event.RecordApplicationEvents

@SpringBootTest
@RecordApplicationEvents
class OrderSpringEventPublisherTest {

    @Autowired
    private lateinit var orderSpringEventPublisher: OrderSpringEventPublisher

    @DisplayName("주문 완료 이벤트를 발행한다.")
    @Test
    fun publish(events: ApplicationEvents) {
        //given
        val event = OrderEventFixture.create()

        //when
        orderSpringEventPublisher.publish(event)

        // then
        val count = events.stream(OrderEvent.Completed::class.java).count()
        assertThat(count).isEqualTo(1)
    }

}