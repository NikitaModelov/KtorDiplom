package ru.modelov.graduatestudents.contoller.user.rules

enum class Rules(val value: String) {
    /***
     * Полные права
     */
    ADMIN("ADMIN"),

    /***
     * Права редактора статей
     */
    EDITOR("EDITOR"),

    /***
     * Права модератора
     */
    MODERATOR("MODERATOR"),

    /***
     * Обычный пользователь
     */
    USER("USER");

    companion object {
        fun findByValue(value: String?): Rules {
            return values().find { it.value == value } ?: USER
        }
    }
}