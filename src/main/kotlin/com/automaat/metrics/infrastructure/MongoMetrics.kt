package com.automaat.metrics.infrastructure

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import org.springframework.stereotype.Component

@Component
class MongoMetrics(meterRegistry: MeterRegistry) {
    val save = MongoOperationMetric(
        mongoTimer(meterRegistry, "save"),
        mongoSuccessCounter(meterRegistry, "save"),
        mongoFailureCounter(meterRegistry, "save")
    )
    val read = MongoOperationMetric(
        mongoTimer(meterRegistry, "read"),
        mongoSuccessCounter(meterRegistry, "read"),
        mongoFailureCounter(meterRegistry, "read")
    )
}

private fun mongoTimer(meterRegistry: MeterRegistry, operation: String) =
    MetricsUtils.createTimerWithQuantiles(
        meterRegistry, "mongo", listOf(Tag.of("operation", operation))
    )

private fun mongoSuccessCounter(meterRegistry: MeterRegistry, operation: String) =
    mongoCounter(meterRegistry, operation, "success")

private fun mongoFailureCounter(meterRegistry: MeterRegistry, operation: String) =
    mongoCounter(meterRegistry, operation, "failure")

private fun mongoCounter(meterRegistry: MeterRegistry, operation: String, status: String) =
    Counter.builder("mongo")
        .tags("operation", operation, "status", status)
        .register(meterRegistry)
