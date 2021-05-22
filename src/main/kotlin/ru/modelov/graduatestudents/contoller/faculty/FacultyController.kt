package ru.modelov.graduatestudents.contoller.faculty

import ru.modelov.graduatestudents.contoller.user.UserController

class FacultyController(
    private val userController: UserController
) {

    suspend fun getFacultyByToken(token: String): String? =
        userController.getUserByTokenMongo(token)?.scope?.faculty
}