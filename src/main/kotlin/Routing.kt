package com.example

import com.example.data.CoffeeService
import com.example.data.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.request.receive

fun Application.configureRouting() {
    val coffeeService = CoffeeService()
    val userService = UserService()

    routing {

        // ============================== БЛОК КОФЕ ==================================

        get("/coffees") {
            val coffees = coffeeService.getAllCoffees()
            call.respond(HttpStatusCode.OK, coffees)
        }

        // Добавить новый кофе
        post("/coffees") {
            try {
                val request = call.receive<CreateCoffeeRequest>()

                val newId = coffeeService.createCoffee(
                    coffeeName = request.name,
                    coffeeType = request.type,
                    coffeeRating = request.rating
                )

                call.respond(HttpStatusCode.Created, "Кофе успешно добавлен с ID: $newId")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Ошибка: ${e.localizedMessage}")
            }
        }

        //========================= БЛОК ПОЛЬЗОВАТЕЛЕЙ =====================================

        // Получить всех пользователей
        get("/users") {
            val users = userService.getAllUsers()
            call.respond(HttpStatusCode.OK, users)
        }

        // Регистрация нового пользователя
        post("/users") {
            try {
                val request = call.receive<CreateUserRequest>()

                val newId = userService.createUser(
                    userName = request.name,
                    userEmail = request.email,
                    userPassword = request.password
                )
                // ОБЯЗАТЕЛЬНО отвечаем клиенту, что всё прошло успешно!
                call.respond(HttpStatusCode.Created, "Пользователь создан с ID: $newId")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Ошибка: ${e.localizedMessage}")
            }
        }
    }
}
// Модели
@kotlinx.serialization.Serializable
data class CreateCoffeeRequest(
    val name: String,
    val type: String,
    val rating: Double
)

@kotlinx.serialization.Serializable
data class CreateUserRequest(
    val name: String,
    val email: String,
    val password: String
)