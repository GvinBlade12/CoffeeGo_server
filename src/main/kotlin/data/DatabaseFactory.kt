package com.example.data

import io.ktor.server.config.ApplicationConfig
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction


object DatabaseFactory {

    fun init(config: ApplicationConfig) {
        val driverClassName = "org.postgresql.Driver"
        val jdbcUrl = config.property("postgres.url").getString()
        val user = config.property("postgres.user").getString()
        val password = config.property("postgres.password").getString()

        val database = Database.connect(jdbcUrl, driverClassName, user, password)


        transaction(database) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(CoffeeTable, UserTable)
        }
    }
}