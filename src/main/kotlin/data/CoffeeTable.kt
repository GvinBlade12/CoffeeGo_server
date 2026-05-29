package com.example.data

import org.jetbrains.exposed.v1.core.Table

object CoffeeTable : Table("coffees") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val type = varchar("type", 50)
    val rating = double("rating")
    val imageUrl = varchar("image_url", 255)
    val description = varchar("description", 500)

    override val primaryKey = PrimaryKey(id)
}