package com.example.data

import org.jetbrains.exposed.v1.core.Table

object FavoritesTable : Table("favorites") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(UserTable.id)
    val coffeeId = integer("coffee_id") // ID товара

    override val primaryKey = PrimaryKey(id)
}