package com.automaat.metrics.infrastructure

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import org.springframework.stereotype.Component

@Component
class EndpointMetrics(meterRegistry: MeterRegistry) {

    val createOrder =
        MetricsUtils.createTimerWithQuantiles(meterRegistry, "endpoint", listOf(Tag.of("name", "createOrder")))
    val getOrder =
        MetricsUtils.createTimerWithQuantiles(meterRegistry, "endpoint", listOf(Tag.of("name", "getOrder")))
    val orderPaid =
        MetricsUtils.createTimerWithQuantiles(meterRegistry, "endpoint", listOf(Tag.of("name", "orderPaid")))
    val orderShipped =
        MetricsUtils.createTimerWithQuantiles(meterRegistry, "endpoint", listOf(Tag.of("name", "orderShipped")))
}
