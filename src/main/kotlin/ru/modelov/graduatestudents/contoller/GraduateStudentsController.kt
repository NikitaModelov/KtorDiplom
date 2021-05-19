package ru.modelov.graduatestudents.contoller

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.modelov.graduatestudents.contoller.user.UserController
import ru.modelov.graduatestudents.model.Faculties
import ru.modelov.graduatestudents.model.Groups
import ru.modelov.graduatestudents.model.student.GraduateStudentDto
import ru.modelov.graduatestudents.model.student.GraduateStudentTable
import ru.modelov.graduatestudents.model.student.mapToGraduateStudentDto
import ru.modelov.graduatestudents.model.students.GraduateStudentCardDto
import ru.modelov.graduatestudents.model.user.User
import ru.modelov.graduatestudents.model.user.UsersTable
import ru.modelov.graduatestudents.model.user.toUser

class GraduateStudentsController(
    private val userController: UserController
) {

    fun getAll(year: String, faculty: String): List<GraduateStudentCardDto> {
        var results: MutableList<GraduateStudentCardDto> = mutableListOf()

        transaction {
            results = (UsersTable innerJoin Groups innerJoin Faculties).select {
                (UsersTable.yearGraduate eq year) and
                        (Faculties.titleFaculty eq faculty)
            }
                .map {
                    GraduateStudentCardDto(
                        id = it[UsersTable.id].toLong(),
                        firstName = it[UsersTable.firstName],
                        secondName = it[UsersTable.secondName],
                        patronymic = it[UsersTable.patronymic],
                        yearGraduate = it[UsersTable.yearGraduate],
                        group = it[UsersTable.group],
                        urlImage = it[UsersTable.imageUrl]
                    )
                }.toMutableList()
        }
        return results
    }

    fun getById(id: Int): User? = userController.getUserById(id)
}
