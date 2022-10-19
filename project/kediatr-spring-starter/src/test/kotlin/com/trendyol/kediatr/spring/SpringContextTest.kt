package com.trendyol.kediatr.spring

import com.trendyol.kediatr.Mediator
import com.trendyol.kediatr.MediatorImpl
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertNotNull

@SpringBootTest(classes = [KediatRConfiguration::class])
class SpringContextTest {

    @Autowired
    lateinit var commandBus: Mediator

    @Test
    fun contextLoads() {
        assertNotNull(commandBus)
        assert(commandBus is MediatorImpl)
    }
}
