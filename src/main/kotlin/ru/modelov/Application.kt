package ru.modelov

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.modelov.graduatestudents.contoller.email.EmailController
import ru.modelov.graduatestudents.contoller.faculty.FacultyController
import ru.modelov.graduatestudents.contoller.password.PasswordController
import ru.modelov.graduatestudents.contoller.token.TokenController
import ru.modelov.graduatestudents.contoller.user.GraduateStudentsController
import ru.modelov.graduatestudents.contoller.user.UserController
import ru.modelov.graduatestudents.routings.graduateStudentRouting
import ru.modelov.graduatestudents.routings.userRouting
import ru.modelov.service.auth.makeJWTVerifier
import ru.modelov.util.extension.Config
import java.time.Duration

fun Long.withOffset(offset: Duration) = this + offset.toMillis()

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    install(Authentication) {
        jwt("access") {
            verifier {
                makeJWTVerifier(Config.algorithm, Config.issuer)
            }

            validate { token ->
                if (token.payload.expiresAt.time > System.currentTimeMillis())
                    JWTPrincipal(token.payload)
                else null
            }
        }
    }

    routing {
        get {
            call.respond("I'm alive!")
        }
    }

    install(ContentNegotiation) {
        gson()
    }
    userRoutes()
    graduateStudentRoutes()
}

fun Application.graduateStudentRoutes() {
    routing {
        graduateStudentRouting(
            graduateStudentsController = GraduateStudentsController(),
            facultyController = FacultyController(
                userController = UserController()
            )
        )
    }
}

fun Application.userRoutes() {
    routing {
        userRouting(
            UserController(),
            EmailController(),
            PasswordController(),
            TokenController()
        )
    }
}
