package ru.modelov.graduatestudents.contoller

import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.modelov.graduatestudents.model.GraduateStudentDto
import ru.modelov.graduatestudents.model.GraduateStudentTable
import ru.modelov.graduatestudents.model.mapToGraduateStudentDto

class GraduateStudentsController {

    fun getAll(): List<GraduateStudentDto> {
        val results: MutableList<GraduateStudentDto> = mutableListOf()
        transaction {
            GraduateStudentTable.selectAll().map {
                results.add(
                    it.mapToGraduateStudentDto()
                )
            }
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
