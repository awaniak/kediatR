package com.trendyol.kediatr.command

import com.trendyol.kediatr.Request

/**
 * Marker interface for a command
 *
 * @since 1.0.0
 * @see CommandHandler
 * @see AsyncCommandHandler
 */
interface Command: Request<Unit>
