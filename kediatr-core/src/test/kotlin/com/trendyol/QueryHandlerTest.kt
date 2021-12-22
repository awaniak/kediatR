package com.trendyol

import com.trendyol.boru.PipelineStep
import com.trendyol.boru.pipelineBuilder
import com.trendyol.boru.usePipelineStepWhen
import com.trendyol.kediatr.CommandBus
import com.trendyol.kediatr.CommandBusBuilder
import com.trendyol.kediatr.KediatrPipelineContext
import com.trendyol.kediatr.KediatrQueryPipelineContext
import com.trendyol.kediatr.exception.HandlerNotFoundException
import com.trendyol.kediatr.query.AsyncQueryHandler
import com.trendyol.kediatr.query.Query
import com.trendyol.kediatr.query.QueryHandler
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class QueryHandlerTest {

    @Test
    fun `queryHandler should retrieve result`() {
        val handler = TestQueryHandler()
        val handlers: HashMap<Class<*>, Any> = hashMapOf(Pair(TestQueryHandler::class.java, handler))
        val provider = ManuelDependencyProvider(handlers)
        val bus: CommandBus = CommandBusBuilder(provider).build()

        val result = bus.executeQuery(TestQuery(1))

        assertTrue {
            result == "hello 1"
        }
    }

    @Test
    fun `async queryHandler should retrieve result`() = runBlocking {
        val handler = AsyncTestQueryHandler()
        val handlers: HashMap<Class<*>, Any> = hashMapOf(Pair(AsyncTestQueryHandler::class.java, handler))
        val provider = ManuelDependencyProvider(handlers)
        val bus: CommandBus = CommandBusBuilder(provider).build()
        val result = bus.executeQueryAsync(TestQuery(1))

        assertTrue {
            result == "hello 1"
        }
    }

    @Test
    fun `should throw exception if given async query has not been registered before`() {
        val handlers: HashMap<Class<*>, Any> = hashMapOf()
        val provider = ManuelDependencyProvider(handlers)
        val bus: CommandBus = CommandBusBuilder(provider).build()

        val exception = assertFailsWith(HandlerNotFoundException::class) {
            runBlocking {
                bus.executeQueryAsync(NonExistQuery())
            }
        }

        assertNotNull(exception)
        assertEquals(exception.message, "handler could not be found for com.trendyol.NonExistQuery")
    }

    @Test
    fun `should throw exception if given query has not been registered before`() {
        val handlers: HashMap<Class<*>, Any> = hashMapOf()
        val provider = ManuelDependencyProvider(handlers)
        val bus: CommandBus = CommandBusBuilder(provider).build()

        val exception = assertFailsWith(HandlerNotFoundException::class) {
            bus.executeQuery(NonExistQuery())
        }

        assertNotNull(exception)
        assertEquals(exception.message, "handler could not be found for com.trendyol.NonExistQuery")
    }

    @Test
    fun `should execute pipelines`() = runBlocking {
        val handler = AsyncTestQueryHandler()
        val handlers: HashMap<Class<*>, Any> = hashMapOf(Pair(AsyncTestQueryHandler::class.java, handler))
        val provider = ManuelDependencyProvider(handlers)

        val beforePipeline = pipelineBuilder<KediatrQueryPipelineContext> {
            usePipelineStepWhen(TestStep()) {
                it.request is TestQuery
            }
        }

        val afterPipeline = pipelineBuilder<KediatrQueryPipelineContext> {
            usePipelineStepWhen(TestStep()) {
                it.request is TestQuery
            }
        }

        val bus: CommandBus = CommandBusBuilder(provider)
            .withBeforeCommandPipeline(beforePipeline)
            .withAfterCommandPipeline(afterPipeline)
            .build()

        val result = bus.executeQueryAsync(TestQuery(1))

        println(result)
    }
}

class TestStep : PipelineStep<KediatrQueryPipelineContext> {
    override suspend fun execute(context: KediatrQueryPipelineContext, next: suspend (KediatrQueryPipelineContext) -> Unit) = with(context.request as TestQuery) {
        println(this.id)
        println(context.result)
        context.result = "holley"
        next(context)
    }
}

class CachingTestStep : PipelineStep<KediatrQueryPipelineContext> {
    private val cache = hashMapOf<String, String>()

    override suspend fun execute(context: KediatrQueryPipelineContext, next: suspend (KediatrQueryPipelineContext) -> Unit) = with(context.request as TestQuery) {
        cache[this.id.toString()] ?: run {
            cache[this.id.toString()] = context.result
            next(context)
        }


        context.result = "holley"
        next(context)
    }
}

class NonExistQuery : Query<String>

class TestQuery(val id: Int) : Query<String>

class TestQueryHandler : QueryHandler<TestQuery, String> {
    override fun handle(query: TestQuery): String {
        return "hello " + query.id
    }
}

class AsyncTestQueryHandler : AsyncQueryHandler<TestQuery, String> {
    override suspend fun handleAsync(query: TestQuery): String {
        return "hello " + query.id
    }
}
