package ru.modelov.graduatestudents.model.user.mongo

import org.bson.codecs.pojo.annotations.BsonId

data class User(
    @BsonId
    val _id: String? = null,
    val firstName: String,
    val secondName: String,
    val patronymic: String?,
    val email: String,
    val password: ByteArray?,
    val locate: String?,
    val birthday: String,
    val achievement: String?,
    val workPlace: String?,
    val imageUrl: String?,
    val yearGraduate: String,
    val scope: ScopeGroup,
    val links: List<String>?,
    val active: Boolean,
    val rule: String
)

fun User.toSecretUser() =
    SecretUser(
        _id = _id,
        firstName = firstName,
        secondName = secondName,
        patronymic = patronymic,
        email = email,
        locate = locate,
        birthday = birthday,
        achievement = achievement,
        workPlace = workPlace,
        links = links,
        imageUrl = imageUrl,
        yearGraduate = yearGraduate,
        scope = scope,
        active = active,
    )

fun User.toUserCard() =
    UserCard(
        _id = _id,
        firstName = firstName,
        secondName = secondName,
        patronymic = patronymic,
        yearGraduate = yearGraduate,
        scope = scope,
        imageUrl = imageUrl
    )
