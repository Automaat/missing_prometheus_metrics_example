package com.automaat.metrics.infrastructure

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Timer

object MetricsUtils {

    fun createTimerWithQuantiles(meterRegistry: MeterRegistry, name: String, tags: List<Tag>): Timer {
        return Timer.builder(name)
            .tags(tags)
            .publishPercentileHistogram(true)
            .publishPercentiles(0.5, 0.9, 0.99)
            .register(meterRegistry)
    }
}
