package ru.modelov

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.Database
import ru.modelov.graduatestudents.contoller.GraduateStudentsController
import ru.modelov.graduatestudents.contoller.GroupController
import ru.modelov.graduatestudents.contoller.email.EmailController
import ru.modelov.graduatestudents.contoller.password.PasswordController
import ru.modelov.graduatestudents.contoller.token.TokenController
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
    initDatabase()

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

    install(ContentNegotiation) {
        gson()
    }
    userRoutes()
    graduateStudentRoutes()
}


private fun initDatabase() {
    val config = HikariConfig("/hikari.properties")
    config.schema = "public"
    val ds = HikariDataSource(config)
    Database.connect(ds)
}

fun Application.graduateStudentRoutes() {
    routing {
        graduateStudentRouting(
            graduateStudentsController = GraduateStudentsController(
                userController = UserController()
            ),
            groupController = GroupController(
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
