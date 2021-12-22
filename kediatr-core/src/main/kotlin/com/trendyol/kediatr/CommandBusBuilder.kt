package com.trendyol.kediatr

import com.trendyol.boru.Pipeline
import com.trendyol.boru.PipelineContext
import com.trendyol.kediatr.notification.PublishStrategy
import com.trendyol.kediatr.notification.StopOnExceptionPublishStrategy

class CommandBusBuilder(
    private val dependencyProvider: DependencyProvider,
) {
    private var publishStrategy: PublishStrategy = StopOnExceptionPublishStrategy()
    private var beforeCommandPipeline: Pipeline<KediatrCommandPipelineContext>? = null
    private var afterCommandPipeline: Pipeline<KediatrCommandPipelineContext>? = null
    private var beforeQueryPipeline: Pipeline<KediatrQueryPipelineContext>? = null
    private var afterQueryPipeline: Pipeline<KediatrQueryPipelineContext>? = null

    /**
     * Overrides default notification publishing strategy.
     * Default strategy is [StopOnExceptionPublishStrategy]
     *
     * @since 1.0.9
     * @see [PublishStrategy]
     * @see [ContinueOnExceptionPublishStrategy]
     * @see [StopOnExceptionPublishStrategy]
     * @see [ParallelNoWaitPublishStrategy]
     * @see [ParallelWhenAllPublishStrategy]
     */
    fun withPublishStrategy(publishStrategy: PublishStrategy): CommandBusBuilder {
        this.publishStrategy = publishStrategy
        return this
    }

    fun withBeforeCommandPipeline(pipeline: Pipeline<KediatrCommandPipelineContext>): CommandBusBuilder {
        beforeCommandPipeline = pipeline
        return this
    }

    fun withAfterCommandPipeline(pipeline: Pipeline<KediatrCommandPipelineContext>): CommandBusBuilder {
        afterCommandPipeline = pipeline
        return this
    }

    fun withBeforeQueryPipeline(pipeline: Pipeline<KediatrQueryPipelineContext>): CommandBusBuilder {
        beforeQueryPipeline = pipeline
        return this
    }

    fun withAfterQueryPipeline(pipeline: Pipeline<KediatrQueryPipelineContext>): CommandBusBuilder {
        afterQueryPipeline = pipeline
        return this
    }

    fun build(registry: Registry = RegistryImpl(dependencyProvider)): CommandBus {
        return CommandBusImpl(registry, publishStrategy, beforeQueryPipeline, afterQueryPipeline, beforeCommandPipeline, afterCommandPipeline)
    }
}

class KediatrQueryPipelineContext(
    var request: Request<*>,
    var result: Any? = null,
) : PipelineContext {
    override val items: Map<Any, Any>
        get() = TODO("Not yet implemented")
}

class KediatrCommandPipelineContext(
    var request: Request<*>,
) : PipelineContext {
    override val items: Map<Any, Any>
        get() = TODO("Not yet implemented")
}