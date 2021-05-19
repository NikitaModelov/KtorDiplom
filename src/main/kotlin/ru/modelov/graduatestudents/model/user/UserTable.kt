package ru.modelov.graduatestudents.model.user

import org.jetbrains.exposed.sql.Table
import ru.modelov.graduatestudents.model.Groups

object UsersTable : Table("users") {
    val email = varchar("email", 50)
    val password = binary("password", 200)
    val active = bool("active")
    val id = integer("id")
    override val primaryKey = PrimaryKey(id)
    val firstName = varchar("firstName", 20)
    val secondName = varchar("secondName", 20)
    val patronymic = varchar("patronymic", 30)
    val locate = varchar("locate", 50)
    val birthday = varchar("birthday", 20)
    val achievement = varchar("achievement", 2000)
    val workPlace = varchar("workPlace", 200)
    val imageUrl = varchar("imageUrl", 2000)
    val yearGraduate = varchar("yearGraduate", 4)
    val group = (varchar("group", 10) references Groups.titleGroup)
}