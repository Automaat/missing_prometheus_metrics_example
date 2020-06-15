package com.automaat.metrics.infrastructure

import com.automaat.metrics.OrderRepository
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import org.springframework.stereotype.Component

@Component
class PaidOrdersStat(private val meterRegistry: MeterRegistry, private val orderRepository: OrderRepository) {

    val paidOrdersCount = meterRegistry.gauge("orders", listOf(Tag.of("status", "paid")), orderRepository) {
        it.countPaidOrders().toDouble()
    }

    val oldestPaidOrder = meterRegistry.gauge("orders_paid", listOf(Tag.of("status", "paid")), orderRepository) {
        it.getLongestTimeMillisInBeforeShipping().toDouble()
    }
}