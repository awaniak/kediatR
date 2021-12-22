package com.trendyol.kediatr.dependency

import com.trendyol.kediatr.DependencyProvider
import com.trendyol.kediatr.notification.AsyncNotificationHandler
import com.trendyol.kediatr.notification.NotificationHandler

internal class NotificationProvider<H : NotificationHandler<*>>(
    private val dependencyProvider: DependencyProvider,
    private val type: Class<H>
) {

    fun get(): H {
        return dependencyProvider.getSingleInstanceOf(type)
    }
}

internal class AsyncNotificationProvider<H : AsyncNotificationHandler<*>>(
    private val dependencyProvider: DependencyProvider,
    private val type: Class<H>
) {

    fun get(): H {
        return dependencyProvider.getSingleInstanceOf(type)
    }
}