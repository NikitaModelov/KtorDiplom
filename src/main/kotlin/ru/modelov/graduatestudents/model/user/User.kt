package ru.modelov.graduatestudents.model.user

import org.jetbrains.exposed.sql.ResultRow
import ru.modelov.graduatestudents.model.Faculties
import ru.modelov.graduatestudents.model.Groups
import ru.modelov.graduatestudents.model.Specialities

data class SecretUser(
    val id: Int,
    val email: String,
    val password: ByteArray,
    val active: Boolean
)

data class User(
    val email: String,
    val active: Boolean,
    val firstName: String,
    val secondName: String,
    val patronymic: String?,
    val locate: String?,
    val birthday: String,
    val achievement: String?,
    val workPlace: String?,
    val imageUrl: String?,
    val yearGraduate: String,
    val group: Group
)

data class Group(
    val group: String,
    val faculty: String,
    val speciality: String
)

fun toUser(row: ResultRow) = User(
    email = row[UsersTable.email],
    active = row[UsersTable.active],
    firstName = row[UsersTable.firstName],
    secondName = row[UsersTable.secondName],
    patronymic = row[UsersTable.patronymic],
    locate = row[UsersTable.locate],
    birthday = row[UsersTable.birthday],
    achievement = row[UsersTable.achievement],
    workPlace = row[UsersTable.workPlace],
    imageUrl = row[UsersTable.imageUrl],
    yearGraduate = row[UsersTable.yearGraduate],
    group = Group(
        group = row[Groups.titleGroup],
        faculty = row[Faculties.titleFaculty],
        speciality = row[Specialities.titleSpeciality]
    )
)