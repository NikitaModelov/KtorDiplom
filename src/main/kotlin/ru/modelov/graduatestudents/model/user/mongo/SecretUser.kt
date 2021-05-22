package ru.modelov.graduatestudents.model.user.mongo

import org.bson.codecs.pojo.annotations.BsonId

/***
 * Модель для клиента без пароля и роли
 */
data class SecretUser(
    @BsonId
    val _id: String? = null,
    val firstName: String,
    val secondName: String,
    val patronymic: String?,
    val email: String,
    val locate: String?,
    val birthday: String,
    val links: List<String>?,
    val achievement: String?,
    val workPlace: String?,
    val imageUrl: String?,
    val yearGraduate: String,
    val scope: ScopeGroup,
    val active: Boolean
)