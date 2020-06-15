package com.automaat.metrics

import com.automaat.metrics.endpoints.CreateOrderRequest
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID

@Document("order")
data class OrderEntity(
    @Id val id: String,
    val value: Int,
    val items: List<String>,
    val status: OrderStatus,
    val modificationDate: LocalDateTime
) {

    companion object {
        fun fromRequest(createOrderRequest: CreateOrderRequest): OrderEntity {
            return OrderEntity(
                id = UUID.randomUUID().toString(),
                items = createOrderRequest.items,
                value = createOrderRequest.value,
                status = OrderStatus.NEW,
                modificationDate = LocalDateTime.now()
            )
        }
    }
}

enum class OrderStatus {
    NEW,
    PAID,
    SHIPPED
}