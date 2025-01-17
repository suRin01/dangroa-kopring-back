package com.deguru.dangroa.global

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import logger
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime





abstract class BaseLongIdTable(name: String, idName: String = "id") : LongIdTable(name, idName) {
    val createdAt = datetime("created_at").clientDefault { Clock.System.now().toLocalDateTime(timeZone = TimeZone.UTC) }
    val updatedAt = datetime("updated_at").clientDefault { Clock.System.now().toLocalDateTime(timeZone = TimeZone.UTC) }
}

abstract class BaseEntity(id: EntityID<Long>, table: BaseLongIdTable) : LongEntity(id) {
    val createdAt by table.createdAt
    var updatedAt by table.updatedAt
}

abstract class BaseEntityClass<E : BaseEntity>(table: BaseLongIdTable) : LongEntityClass<E>(table) {
    private val log = logger()
    init {
        EntityHook.subscribe { action ->
            if (action.changeType == EntityChangeType.Updated) {
                try {
                    action.toEntity(this)?.updatedAt = Clock.System.now().toLocalDateTime(timeZone = TimeZone.UTC)
                } catch (e: Exception) {
                    log.warn("Failed to update entity $this updatedAt", e.message)
                }
            }
        }
    }
}
