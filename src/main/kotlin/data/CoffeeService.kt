package com.example.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

//ФУНКЦИЯ ДОБАВЛЕНИЯ НОВОГО ВИНА
class CoffeeService{
    fun createCoffee(coffeeName: String, coffeeType: String, coffeeRating: Double): Int = transaction {

        // Сохраняем результат вставки в переменную
        val insertedRow = CoffeeTable.insert {
            it[name] = coffeeName
            it[type] = coffeeType
            it[rating] = coffeeRating
        }

        // Достаем сгенерированный базой ID и возвращаем его
        insertedRow[CoffeeTable.id]
    }
//ПОЛУЧЕНИЕ СПИСКА ВСЕХ ВИН
    fun getAllCoffees(): List<CoffeeModel> = transaction {
        CoffeeTable.selectAll().map{ row ->
            CoffeeModel(
                id = row[CoffeeTable.id],
                name = row[CoffeeTable.name],
                type = row[CoffeeTable.type],
                rating = row[CoffeeTable.rating]
            )
        }
    }
}
//Дата-моделька для передачи данных
@Serializable
data class CoffeeModel(
    val id: Int,
    val name: String,
    val type: String,
    val rating: Double
)