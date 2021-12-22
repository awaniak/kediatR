package com.trendyol.kediatr

import com.trendyol.boru.Pipeline
import com.trendyol.kediatr.command.Command
import com.trendyol.kediatr.command.CommandWithResult
import com.trendyol.kediatr.notification.Notification
import com.trendyol.kediatr.notification.PublishStrategy
import com.trendyol.kediatr.notification.StopOnExceptionPublishStrategy
import com.trendyol.kediatr.query.Query

class CommandBusImpl(
    private val registry: Registry,
    private val publishStrategy: PublishStrategy = StopOnExceptionPublishStrategy(),
    private val beforeQueryPipeline: Pipeline<KediatrQueryPipelineContext>?,
    private val afterQueryPipeline: Pipeline<KediatrQueryPipelineContext>?,
    private val beforeCommandPipeline: Pipeline<KediatrCommandPipelineContext>?,
    private val afterCommandPipeline: Pipeline<KediatrCommandPipelineContext>?,
) : CommandBus {

    override fun <TQuery : Query<TResponse>, TResponse> executeQuery(query: TQuery): TResponse = processPipeline(registry.getPipelineBehaviors(), query) {
        registry.resolveQueryHandler(query.javaClass).handle(query)
    }

    override fun <TCommand : Command> executeCommand(command: TCommand) = processPipeline(registry.getPipelineBehaviors(), command) {
        registry.resolveCommandHandler(command.javaClass).handle(command)
    }

    override fun <TCommand : CommandWithResult<TResult>, TResult> executeCommand(command: TCommand): TResult = processPipeline(registry.getPipelineBehaviors(), command) {
        registry.resolveCommandWithResultHandler(command.javaClass).handle(command)
    }

    override fun <T : Notification> publishNotification(notification: T) = processPipeline(registry.getPipelineBehaviors(), notification) {
        publishStrategy.publish(notification, registry.resolveNotificationHandlers(notification.javaClass))
    }

    override suspend fun <TQuery : Query<TResponse>, TResponse> executeQueryAsync(query: TQuery): TResponse {
        val context = KediatrQueryPipelineContext(query)
        beforeQueryPipeline?.execute(context)
        var result = registry.resolveAsyncQueryHandler(query.javaClass).handleAsync(query)

        afterQueryPipeline?.let { pipeline ->
            context.result = result
            pipeline.execute(context)
            result = context.result as? TResponse ?: throw Exception("Result is not of type TResponse")
        }

        return result
    }

    override suspend fun <TCommand : Command> executeCommandAsync(command: TCommand) = processAsyncPipeline(registry.getAsyncPipelineBehaviors(), command) {
        registry.resolveAsyncCommandHandler(command.javaClass).handleAsync(command)
    }

    override suspend fun <TCommand : CommandWithResult<TResult>, TResult> executeCommandAsync(command: TCommand): TResult = processAsyncPipeline(registry.getAsyncPipelineBehaviors(), command) {
        registry.resolveAsyncCommandWithResultHandler(command.javaClass).handleAsync(command)
    }

    override suspend fun <T : Notification> publishNotificationAsync(notification: T) = processAsyncPipeline(registry.getAsyncPipelineBehaviors(), notification) {
        publishStrategy.publishAsync(notification, registry.resolveAsyncNotificationHandlers(notification.javaClass))
    }
}