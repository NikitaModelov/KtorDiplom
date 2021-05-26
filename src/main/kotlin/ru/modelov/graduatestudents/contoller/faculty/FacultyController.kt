package ru.modelov.graduatestudents.contoller.faculty

import ru.modelov.graduatestudents.contoller.user.UserController
import ru.modelov.graduatestudents.model.user.mongo.ScopeGroup

class FacultyController(
    private val userController: UserController
) {

    suspend fun getFacultyByToken(token: String): String? =
        userController.getUserByTokenMongo(token)?.scope?.faculty

    suspend fun getGroupByToken(token: String): String? =
        userController.getUserByTokenMongo(token)?.scope?.group

    suspend fun getScopeByToken(token: String): ScopeGroup? =
        userController.getUserByTokenMongo(token)?.scope
}