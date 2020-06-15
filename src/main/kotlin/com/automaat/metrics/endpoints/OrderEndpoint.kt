package com.automaat.metrics.endpoints

import com.automaat.metrics.OrderEntity
import com.automaat.metrics.OrderService
import com.automaat.metrics.OrderStatus
import com.automaat.metrics.infrastructure.EndpointMetrics
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/orders")
class OrderEndpoint(private val orderService: OrderService, private val endpointMetrics: EndpointMetrics) {

    @PostMapping
    fun createOrder(@RequestBody createOrderRequest: CreateOrderRequest): ResponseEntity<CreateOrderResponse> {
        return endpointMetrics.createOrder.record<ResponseEntity<CreateOrderResponse>> {
            val orderId = orderService.createOrder(createOrderRequest)

            ResponseEntity.ok(
                CreateOrderResponse(orderId)
            )
        }

    }

    @GetMapping("/{orderId}")
    fun getOrder(@PathVariable orderId: String): ResponseEntity<GetOrderResponse> {
        return endpointMetrics.getOrder.record<ResponseEntity<GetOrderResponse>> {
            ResponseEntity.ok(
                GetOrderResponse.fromEntity(
                    orderService.findOrder(orderId)
                )
            )
        }
    }

    @PostMapping("/{orderId}/paid")
    fun orderPaid(@PathVariable orderId: String): ResponseEntity<Any> {
        return endpointMetrics.orderPaid.record<ResponseEntity<Any>> {
            orderService.updateOrderWithStatus(orderId, OrderStatus.PAID)
            ResponseEntity.ok().build()
        }
    }

    @PostMapping("/{orderId}/shipped")
    fun orderShipped(@PathVariable orderId: String): ResponseEntity<Any> {
        return endpointMetrics.orderShipped.record<ResponseEntity<Any>> {
            orderService.updateOrderWithStatus(orderId, OrderStatus.SHIPPED)
            ResponseEntity.ok().build()
        }
    }
}

data class CreateOrderRequest(val items: List<String>, val value: Int)
data class CreateOrderResponse(val id: String)

data class GetOrderResponse(
    val id: String,
    val value: Int,
    val items: List<String>,
    val status: OrderStatus,
    val modificationDate: LocalDateTime
) {
    companion object {
        fun fromEntity(orderEntity: OrderEntity): GetOrderResponse {
            return GetOrderResponse(
                id = orderEntity.id,
                value = orderEntity.value,
                items = orderEntity.items,
                status = orderEntity.status,
                modificationDate = orderEntity.modificationDate
            )
        }
    }
}