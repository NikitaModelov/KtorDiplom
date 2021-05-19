package ru.modelov.graduatestudents.contoller.password

class PasswordController {

    fun generatePassword(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..LENGTH)
            .map { allowedChars.random() }
            .joinToString("")
    }

    companion object {
        private const val LENGTH = 12
    }
}