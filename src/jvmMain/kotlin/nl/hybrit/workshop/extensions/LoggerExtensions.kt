package nl.hybrit.workshop.extensions

import org.slf4j.Logger

/**
 * Log trace message, if log level 'TRACE' is enabled.
 *
 * @param message the message to log.
 */
fun Logger.trace(message: () -> String): Unit =
        if (this.isTraceEnabled) this.trace(message()) else Unit

/**
 * Log debug message, it log level 'DEBUG' is enabled.
 *
 * @param message the message to log.
 */
fun Logger.debug(message: () -> String): Unit =
        if (this.isDebugEnabled) this.debug(message()) else Unit

/**
 * Log info message, if log level 'INFO' is enabled.
 *
 * @param message the message to log.
 */
fun Logger.info(message: () -> String): Unit =
        if (this.isInfoEnabled) info(message()) else Unit

/**
 * Log warning message without throwable, if log level 'WARN' is enabled.
 *
 * @param message the message to log.
 */
fun Logger.warn(message: () -> String): Unit =
        if (this.isWarnEnabled) warn(message()) else Unit

/**
 * Log warning message with throwable, if log level 'WARN' is enabled.
 *
 * @param message the message to log.
 */
fun Logger.warn(message: () -> String, throwable: () -> Throwable): Unit =
        if (this.isWarnEnabled) warn(message(), throwable()) else Unit

/**
 * Log error message without throwable, if log level 'ERROR is enabled.
 *
 * @param message the message to log.
 */
fun Logger.error(message: () -> String): Unit =
        if (this.isErrorEnabled) error(message()) else Unit
/**
 * Log error message with throwable, if log level 'ERROR is enabled.
 *
 * @param message the message to log.
 */
fun Logger.error(message: () -> String, throwable: () -> Throwable): Unit =
        if (this.isErrorEnabled) error(message(), throwable()) else Unit
