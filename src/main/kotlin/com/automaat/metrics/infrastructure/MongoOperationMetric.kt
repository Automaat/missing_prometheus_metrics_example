package com.automaat.metrics.infrastructure

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Timer

class MongoOperationMetric(
    private val mongoTimer: Timer,
    private val mongoSuccessCounter: Counter,
    private val mongoFailureCounter: Counter
) {

    fun <T> metered(function: () -> T): T {
        try {
            val result = timed(function)
            success()
            return result
        } catch (e: Exception) {
            failure()
            throw e
        }
    }

    private fun <T> timed(function: () -> T): T {
        return mongoTimer.record(function)
    }

    private fun success() {
        mongoSuccessCounter.increment()
    }

    private fun failure() {
        mongoFailureCounter.increment()
    }
}