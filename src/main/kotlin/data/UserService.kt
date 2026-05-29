package com.example.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class UserService {

    fun createUser(userEmail: String, userPassword: String, userName: String): Int = transaction {
        val insertedRow = UserTable.insert {
            it[name] = userName
            it[email] = userEmail
            it[password] = userPassword
        }
        insertedRow[UserTable.id]
    }

    fun getAllUsers(): List<UserModel> = transaction {
        UserTable.selectAll().map { row ->
            UserModel(
                id = row[UserTable.id],
                name = row[UserTable.name],
                email = row[UserTable.email],
                password = row[UserTable.password]
            )
        }
    }

    // 1. Добавить товар в избранное
    fun addFavorite(uId: Int, cId: Int): Boolean = transaction {
        // Меняем .select { ... } на .selectAll().where { ... }
        val alreadyExists = UserFavouritesTable
            .selectAll()
            .where { (UserFavouritesTable.userId eq uId) and (UserFavouritesTable.coffeeId eq cId) }
            .count() > 0

        if (!alreadyExists) {
            UserFavouritesTable.insert {
                it[userId] = uId
                it[coffeeId] = cId
            }
            true
        } else {
            false // Уже добавлено ранее
        }
    }

    // 2. Удалить товар из избранного
    fun removeFavorite(uId: Int, cId: Int): Boolean = transaction {
        // Добавляем явное указание "where = " перед фигурными скобками
        val deletedRows = UserFavouritesTable.deleteWhere {
            (UserFavouritesTable.userId eq uId) and (UserFavouritesTable.coffeeId eq cId)
        }
        deletedRows > 0
    }

    // 3. Получить список ID всех любимых кофе конкретного юзера
    fun getFavoritesForUser(uId: Int): List<Int> = transaction {
        // Здесь тоже меняем на .selectAll().where { ... }
        UserFavouritesTable
            .selectAll()
            .where { UserFavouritesTable.userId eq uId }
            .map { row -> row[UserFavouritesTable.coffeeId] } // Вытаскиваем только IDшники кофе
    }
}

@Serializable
data class UserModel(
    val id: Int,
    val name: String,
    val email: String,
    val password: String
)
