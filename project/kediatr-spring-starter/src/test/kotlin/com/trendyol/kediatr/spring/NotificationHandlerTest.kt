package com.trendyol.kediatr.spring

import com.trendyol.kediatr.NotificationHandler
import com.trendyol.kediatr.Mediator
import com.trendyol.kediatr.Notification
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertTrue

var notificationTestCounter = 0
var asyncNotificationTestCounter = 0

@SpringBootTest(
    classes = [
        KediatRConfiguration::class,
        MyFirstNotificationHandler::class
    ]
)
class NotificationHandlerTest {

    init {
        notificationTestCounter = 0
        asyncNotificationTestCounter = 0
    }

    @Autowired
    lateinit var mediator: Mediator

    @Test
    fun `async notificationHandler should be fired`() = runBlocking {
        mediator.publish(MyNotification())

        assertTrue {
            asyncNotificationTestCounter == 1
        }
    }
}

class MyNotification : Notification

class MyFirstNotificationHandler : NotificationHandler<MyNotification> {
    override suspend fun handle(notification: MyNotification) {
        delay(500)
        asyncNotificationTestCounter++
    }
}
