package ru.modelov.graduatestudents.contoller

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.modelov.graduatestudents.model.student.GraduateStudentDto
import ru.modelov.graduatestudents.model.student.GraduateStudentTable
import ru.modelov.graduatestudents.model.student.mapToGraduateStudentDto
import ru.modelov.graduatestudents.model.students.GraduateStudentCardDto

class GraduateStudentsController {

    fun getAll(year: String, faculty: String): List<GraduateStudentCardDto> {
        var results: MutableList<GraduateStudentCardDto> = mutableListOf()
        transaction {
            results = GraduateStudentTable.select {
                (GraduateStudentTable.faculty eq faculty) and
                        (GraduateStudentTable.yearGraduate eq year)
            }
                .map {
                    GraduateStudentCardDto(
                        id = it[GraduateStudentTable.uid],
                        firstName = it[GraduateStudentTable.firstName],
                        secondName = it[GraduateStudentTable.secondName],
                        patronymic = it[GraduateStudentTable.patronymic],
                        yearGraduate = it[GraduateStudentTable.yearGraduate],
                        group = it[GraduateStudentTable.group],
                        urlImage = it[GraduateStudentTable.urlImage]
                    )
                }.toMutableList()
        }
        return results
    }

    fun getById(id: Long): GraduateStudentDto? {
        var result: GraduateStudentDto? = null
        transaction {
            result = GraduateStudentTable.select {
                GraduateStudentTable.uid eq id
            }.map {
                it.mapToGraduateStudentDto()
            }.firstOrNull()
        }
        return result
    }
}
