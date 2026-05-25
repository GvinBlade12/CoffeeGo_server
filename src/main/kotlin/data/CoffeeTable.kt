package com.example.data

import org.jetbrains.exposed.v1.core.Table

object CoffeeTable : Table("coffees") {
    val id = integer("id").autoIncrement()
    val name  = varchar("name", 255)
    val type = varchar("type", 50)
    val rating = double("rating")

    override val primaryKey = PrimaryKey(id)
}