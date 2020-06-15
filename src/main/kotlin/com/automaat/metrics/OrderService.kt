package com.automaat.metrics

import com.automaat.metrics.endpoints.CreateOrderRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderService(private val orderRepository: OrderRepository) {

    fun findOrder(orderId: String): OrderEntity {
        return orderRepository.getOrder(orderId) ?: throw RuntimeException("order not found")
    }

    fun createOrder(createOrderRequest: CreateOrderRequest): String {
        return orderRepository.insertOrder(OrderEntity.fromRequest(createOrderRequest))
    }

    fun updateOrderWithStatus(orderId: String, status: OrderStatus) {
        orderRepository.getOrder(orderId)?.let {
            val updatedOrder = it.copy(
                status = status,
                modificationDate = LocalDateTime.now()
            )

            orderRepository.updateOrder(updatedOrder)
        }
    }
}