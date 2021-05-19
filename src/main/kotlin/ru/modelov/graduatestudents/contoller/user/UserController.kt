package ru.modelov.graduatestudents.contoller.user

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.modelov.graduatestudents.model.Faculties
import ru.modelov.graduatestudents.model.Groups
import ru.modelov.graduatestudents.model.Specialities
import ru.modelov.graduatestudents.model.user.*
import ru.modelov.service.auth.Tokens

class UserController {

    fun createUser(newUser: User, password: ByteArray) = transaction {
        UsersTable.insert {
            it[email] = newUser.email
            it[this.password] = password
            it[active] = newUser.active
            it[firstName] = newUser.firstName
            it[secondName] = newUser.secondName
            it[patronymic] = newUser.patronymic ?: ""
            it[locate] = newUser.locate ?: ""
            it[birthday] = newUser.birthday
            it[achievement] = newUser.achievement ?: ""
            it[workPlace] = newUser.workPlace ?: ""
            it[imageUrl] = newUser.imageUrl ?: ""
            it[yearGraduate] = newUser.yearGraduate
            it[group] = newUser.group.group
        }
    }

    fun getUserByToken(token: String): User = transaction {
        (Tokens innerJoin UsersTable innerJoin Groups innerJoin Faculties innerJoin Specialities).select {
            (Tokens.token eq token) and
                    (Tokens.userId eq UsersTable.id) and
                    (UsersTable.group eq Groups.titleGroup) and
                    (Groups.faculty eq Faculties.id) and
                    (Groups.speciality eq Specialities.id)
        }.mapNotNull {
            toUser(it)
        }.single()
    }

    fun getUserById(id: Int): User = transaction {
        (UsersTable innerJoin Groups innerJoin Faculties innerJoin Specialities).select {
            (UsersTable.id eq id) and
                    (UsersTable.group eq Groups.titleGroup) and
                    (Groups.faculty eq Faculties.id) and
                    (Groups.speciality eq Specialities.id)
        }.mapNotNull {
            toUser(it)
        }.single()
    }

    fun getUserByEmail(email: String): SecretUser? = transaction {
        UsersTable.select {
            (UsersTable.email eq email)
        }.mapNotNull { toSecretUser(it) }
            .singleOrNull()
    }

    private fun toSecretUser(row: ResultRow): SecretUser =
        SecretUser(
            id = row[UsersTable.id],
            email = row[UsersTable.email],
            password = row[UsersTable.password],
            active = row[UsersTable.active]
        )
}