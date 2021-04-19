package ru.modelov.graduatestudents.model

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object GraduateStudentTable : Table(name = "students") {

    val uid = long("id").autoIncrement()
    val firstName = varchar("first_name", 20)
    val secondName = varchar("second_name", 20)
    val patronymic = varchar("patronymic", 20)
    val yearGraduate = varchar("year_graduate", 4)
    val group = varchar("f_group", 10)
    val locate = varchar("locate", 20)
    val birthDay = varchar("birth_day", 20)
    val faculty = varchar("faculty", 50)
    val speciality = varchar("speciality", 100)
    val placeWork = varchar("place_work", 50)
    val achievement = varchar("achievement", 200)
    val urlImage = varchar("url_image", 1000)

    override val primaryKey: PrimaryKey = PrimaryKey(uid, name = "id")
}

fun ResultRow.mapToGraduateStudentDto(): GraduateStudentDto {
    return with(GraduateStudentTable) {
        GraduateStudentDto(
            id = this@mapToGraduateStudentDto[this.uid],
            firstName = this@mapToGraduateStudentDto[this.firstName],
            secondName = this@mapToGraduateStudentDto[this.secondName],
            patronymic = this@mapToGraduateStudentDto[this.patronymic],
            yearGraduate = this@mapToGraduateStudentDto[this.yearGraduate],
            group = this@mapToGraduateStudentDto[this.group],
            locate = this@mapToGraduateStudentDto[this.locate],
            birthDay = this@mapToGraduateStudentDto[this.birthDay],
            faculty = this@mapToGraduateStudentDto[this.faculty],
            speciality = this@mapToGraduateStudentDto[this.speciality],
            placeWork = this@mapToGraduateStudentDto[this.placeWork],
            achievement = this@mapToGraduateStudentDto[this.achievement],
            urlImage = this@mapToGraduateStudentDto[this.urlImage]
        )
    }
}