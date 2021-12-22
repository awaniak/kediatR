package com.trendyol.kediatr.query

import com.trendyol.kediatr.Request

/**
 * Marker interface for a query
 *
 * @since 1.0.0
 * @see QueryHandler
 * @see AsyncQueryHandler
 */
interface Query<TResponse> : Request<TResponse>