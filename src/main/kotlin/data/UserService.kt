package com.example.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class UserService {
    fun createUser(userEmail: String, userPassword: String, userName: String): Int = transaction  {
        val insertedRow = UserTable.insert{
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

}

@Serializable
data class UserModel(
    val id: Int,
    val name: String,
    val email: String,
    val password: String
)