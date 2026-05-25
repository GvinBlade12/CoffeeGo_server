package com.example

import com.example.data.DatabaseFactory
import com.example.data.CoffeeService
import com.example.data.UserService
import io.ktor.server.application.Application

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    //Инициализация базы данных, конфиг беру из application.yaml
    DatabaseFactory.init(environment.config)

    // Создаем таблицу в базе, если её ещё нет
    val coffeeService = CoffeeService()
    val userService = UserService()
}