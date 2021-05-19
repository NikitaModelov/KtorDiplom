package ru.modelov.graduatestudents.contoller

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.modelov.graduatestudents.contoller.user.UserController
import ru.modelov.graduatestudents.model.Faculties
import ru.modelov.graduatestudents.model.Groups
import ru.modelov.graduatestudents.model.Specialities

class GroupController(
    private val userController: UserController
) {

    fun getFacultyByGroup(group: String): String = transaction {
        (Groups innerJoin Faculties).select {
            (Groups.titleGroup eq group) and (Groups.faculty eq Faculties.id)
        }.mapNotNull {
            it[Faculties.titleFaculty].toString()
        }.single()
    }

    fun getFacultyByToken(token: String): String = userController.getUserByToken(token).group.faculty
}