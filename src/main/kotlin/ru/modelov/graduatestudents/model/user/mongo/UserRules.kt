package ru.modelov.graduatestudents.model.user.mongo

import ru.modelov.graduatestudents.contoller.user.rules.Rules

data class UserRules(
    val _id: String,
    val rules: List<String>
)

fun UserRules.getRules(): List<Rules> = rules.map {
    Rules.findByValue(it)
}