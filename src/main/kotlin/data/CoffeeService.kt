package com.example.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class CoffeeService {

    // Добавление нового кофе (теперь с картинкой)
    fun createCoffee(coffeeName: String, coffeeType: String, coffeeRating: Double, coffeeImageUrl: String,
                     coffeeDescription: String): Int = transaction {
        val insertedRow = CoffeeTable.insert {
            it[name] = coffeeName
            it[type] = coffeeType
            it[rating] = coffeeRating
            it[imageUrl] = coffeeImageUrl
            it[description] = coffeeDescription
        }
        insertedRow[CoffeeTable.id]
    }

    // Получение списка всего кофе
    fun getAllCoffees(): List<CoffeeModel> = transaction {
        CoffeeTable.selectAll().map { row ->
            CoffeeModel(
                id = row[CoffeeTable.id],
                name = row[CoffeeTable.name],
                type = row[CoffeeTable.type],
                rating = row[CoffeeTable.rating],
                imageUrl = row[CoffeeTable.imageUrl],
                description = row[CoffeeTable.description]// Вытаскиваем URL из базы
            )
        }
    }
}

// Дата-моделька для передачи данных (добавили imageUrl)
@Serializable
data class CoffeeModel(
    val id: Int,
    val name: String,
    val type: String,
    val rating: Double,
    val imageUrl: String,
    val description: String
)