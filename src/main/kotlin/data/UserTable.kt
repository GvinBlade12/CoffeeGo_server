package com.example.data

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table

object UserTable : Table("users") {
    val id = integer("id").autoIncrement()
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 255)
    val name = varchar("name", 255)

    override val primaryKey = PrimaryKey(id)
}

object UserFavouritesTable : Table("user_favourites") {
    val userId = integer("user_id").references(UserTable.id, onDelete = ReferenceOption.CASCADE)
    val coffeeId = integer("coffee_id").references(CoffeeTable.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(userId, coffeeId)
}

object UserTabsTable : Table("user_tabs") {
    // Сюда тоже добавляем каскадное удаление, чтобы не плодить мусор в БД
    val userId = integer("user_id").references(UserTable.id, onDelete = ReferenceOption.CASCADE)
    val coffeeId = integer("coffee_id").references(CoffeeTable.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(userId, coffeeId)
}