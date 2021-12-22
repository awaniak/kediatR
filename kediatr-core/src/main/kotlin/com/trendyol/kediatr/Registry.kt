package com.trendyol.kediatr

import com.trendyol.kediatr.command.*
import com.trendyol.kediatr.notification.AsyncNotificationHandler
import com.trendyol.kediatr.notification.Notification
import com.trendyol.kediatr.notification.NotificationHandler
import com.trendyol.kediatr.pipeline.AsyncPipelineBehavior
import com.trendyol.kediatr.pipeline.PipelineBehavior
import com.trendyol.kediatr.query.AsyncQueryHandler
import com.trendyol.kediatr.query.Query
import com.trendyol.kediatr.query.QueryHandler

interface Registry {
    fun <TCommand : Command> resolveCommandHandler(classOfCommand: Class<TCommand>): CommandHandler<TCommand>

    fun <TCommand : CommandWithResult<TResult>, TResult> resolveCommandWithResultHandler(classOfCommand: Class<TCommand>): CommandWithResultHandler<TCommand, TResult>

    fun <TQuery : Query<TResult>, TResult> resolveQueryHandler(classOfQuery: Class<TQuery>): QueryHandler<TQuery, TResult>

    fun <TNotification : Notification> resolveNotificationHandlers(classOfNotification: Class<TNotification>): Collection<NotificationHandler<TNotification>>

    fun <TCommand : Command> resolveAsyncCommandHandler(classOfCommand: Class<TCommand>): AsyncCommandHandler<TCommand>

    fun <TCommand : CommandWithResult<TResult>, TResult> resolveAsyncCommandWithResultHandler(classOfCommand: Class<TCommand>): AsyncCommandWithResultHandler<TCommand, TResult>

    fun <TQuery : Query<TResult>, TResult> resolveAsyncQueryHandler(classOfQuery: Class<TQuery>): AsyncQueryHandler<TQuery, TResult>

    fun <TNotification : Notification> resolveAsyncNotificationHandlers(classOfNotification: Class<TNotification>): Collection<AsyncNotificationHandler<TNotification>>

    fun getPipelineBehaviors(): Collection<PipelineBehavior>

    fun getAsyncPipelineBehaviors(): Collection<AsyncPipelineBehavior>
}