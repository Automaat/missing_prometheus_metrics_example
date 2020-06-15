package com.automaat.metrics

import com.automaat.metrics.infrastructure.MongoMetrics
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import java.time.Duration
import java.time.LocalDateTime

@Repository
class OrderRepository(private val mongoOperations: MongoOperations, private val mongoMetrics: MongoMetrics) {

    fun insertOrder(orderEntity: OrderEntity): String {
        mongoMetrics.save.metered {
            mongoOperations.insert(orderEntity)
        }

        return orderEntity.id
    }

    fun updateOrder(orderEntity: OrderEntity) {
        mongoMetrics.save.metered {
            mongoOperations.save(orderEntity)
        }
    }

    fun getOrder(id: String): OrderEntity? {
        return mongoMetrics.read.metered {
            mongoOperations.findById(id, OrderEntity::class.java)
        }
    }

    fun getLongestTimeMillisInBeforeShipping(): Long {
        val oldestPaidOrderQuery =
            Query.query(
                Criteria.where("status").`is`(OrderStatus.PAID)
            ).with(Sort.by("modificationDate")).limit(1)

        return mongoOperations.findOne(oldestPaidOrderQuery, OrderEntity::class.java)
            ?.let { order ->
                Duration.between(order.modificationDate, LocalDateTime.now()).toMillis()
            } ?: 0
    }


    fun countPaidOrders(): Long {
        val oldestPaidOrderQuery =
            Query.query(
                Criteria.where("status").`is`(OrderStatus.PAID)
            )

        return mongoOperations.count(oldestPaidOrderQuery, "order")
    }
}