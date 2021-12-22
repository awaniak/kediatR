package com.trendyol.kediatr.dependency

import com.trendyol.kediatr.DependencyProvider
import com.trendyol.kediatr.query.AsyncQueryHandler
import com.trendyol.kediatr.query.QueryHandler

/**
 * QueryProvider creates a query handler with enabled spring injection.
 *
 * @param <H> type of handler
</H> */
internal class QueryProvider<H : QueryHandler<*, *>>(
    private val dependencyProvider: DependencyProvider,
    private val type: Class<H>
) {

    fun get(): H {
        return dependencyProvider.getSingleInstanceOf(type)
    }
}

/**
 * QueryProvider creates a async query handler with enabled spring injection.
 *
 * @param <H> type of handler
</H> */
internal class AsyncQueryProvider<H : AsyncQueryHandler<*, *>>(
    private val dependencyProvider: DependencyProvider,
    private val type: Class<H>
) {

    fun get(): H {
        return dependencyProvider.getSingleInstanceOf(type)
    }
}