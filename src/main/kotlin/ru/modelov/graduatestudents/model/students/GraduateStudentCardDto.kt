package ru.modelov.graduatestudents.model.students

data class GraduateStudentCardDto(
    val id: Long,
    val firstName: String,
    val secondName: String,
    val patronymic: String,
    val yearGraduate: String,
    val group: String,
    val urlImage: String
)
