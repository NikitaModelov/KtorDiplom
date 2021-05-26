package ru.modelov.util.extension

import com.auth0.jwt.algorithms.Algorithm

object Config {

    const val issuer: String = "ktor"
    val algorithm = Algorithm.HMAC256("very_hard")
    const val accessLifetime = 3500L    // minutes
    const val refreshLifetime = 175L  // days
}