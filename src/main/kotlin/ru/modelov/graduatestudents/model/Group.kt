package ru.modelov.graduatestudents.model

import org.jetbrains.exposed.sql.Table

object Groups : Table("groups") {
    val titleGroup = varchar("title", 10)
    override val primaryKey = PrimaryKey(titleGroup)
    val faculty = (integer("faculty") references Faculties.id)
    val speciality = (integer("speciality") references Specialities.id)

}

object Faculties: Table("faculty") {
    val id = integer("uid")
    override val primaryKey = PrimaryKey(id)
    val titleFaculty = varchar("title", 500)
}

object Specialities: Table("speciality") {
    val id = integer("uid")
    override val primaryKey = PrimaryKey(id)
    val titleSpeciality = varchar("title", 550)
}