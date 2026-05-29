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
        //Добавить новый кофе
        post("/coffees") {
            try {
                val request = call.receive<CreateCoffeeRequest>()

                val newId = coffeeService.createCoffee(
                    coffeeName = request.name,
                    coffeeType = request.type,
                    coffeeRating = request.rating,
                    coffeeImageUrl = request.imageUrl, //  Исправили имя параметра и передаем ссылку
                    coffeeDescription = request.description
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
        get("/favorites/{userId}") {
            val userId = call.parameters["userId"]?.toIntOrNull()
            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Некорректный ID пользователя")
                return@get
            }

            val favorites = userService.getFavoritesForUser(userId)
            call.respond(HttpStatusCode.OK, favorites) // Возвращает массив чисел, например: [1, 4]
        }

// 2. Добавить кофе в избранное
        post("/favorites") {
            try {
                val request = call.receive<FavoriteRequest>()
                val isAdded = userService.addFavorite(request.userId, request.coffeeId)

                if (isAdded) {
                    call.respond(HttpStatusCode.Created, "Товар добавлен в избранное")
                } else {
                    call.respond(HttpStatusCode.Conflict, "Товар уже находится в избранном")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Ошибка: ${e.localizedMessage}")
            }
        }

// 3. Удалить кофе из избранного
        delete("/favorites") {
            try {
                val request = call.receive<FavoriteRequest>()
                val isRemoved = userService.removeFavorite(request.userId, request.coffeeId)

                if (isRemoved) {
                    call.respond(HttpStatusCode.OK, "Товар удален из избранного")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Товар не найден в избранном")
                }
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
    val rating: Double,
    val imageUrl: String,
    val description: String
)

@kotlinx.serialization.Serializable
data class CreateUserRequest(
    val name: String,
    val email: String,
    val password: String
)
@kotlinx.serialization.Serializable
data class FavoriteRequest(
    val userId: Int,
    val coffeeId: Int
)