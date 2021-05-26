package ru.modelov.graduatestudents.model.user.mongo

data class UserCard(
    val _id: String?,
    val firstName: String,
    val secondName: String,
    val patronymic: String?,
    val email: String?,
    val yearGraduate: String,
    val scope: ScopeGroup,
    val imageUrl: String?
)
