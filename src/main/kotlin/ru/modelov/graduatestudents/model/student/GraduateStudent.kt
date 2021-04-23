package ru.modelov.graduatestudents.model.student

data class GraduateStudentDto(
    val id: Long,
    val firstName: String,
    val secondName: String,
    val patronymic: String,
    val yearGraduate: String,
    val group: String,
    val locate: String,
    val birthDay: String,
    val faculty: String,
    val speciality: String,
    val placeWork: String,
    val achievement: String?,
    val urlImage: String
)