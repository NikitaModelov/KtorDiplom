package ru.modelov.graduatestudents.routings

import com.toxicbakery.bcrypt.Bcrypt
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.modelov.graduatestudents.contoller.email.EmailController
import ru.modelov.graduatestudents.contoller.password.PasswordController
import ru.modelov.graduatestudents.contoller.token.TokenController
import ru.modelov.graduatestudents.contoller.user.UserController
import ru.modelov.graduatestudents.model.user.User
import ru.modelov.service.auth.LoginUser
import ru.modelov.service.auth.RefreshToken
import ru.modelov.service.auth.Tokens
import ru.modelov.service.auth.toToken
import ru.modelov.util.extension.Config
import ru.modelov.withOffset
import java.time.Duration

fun Route.userRouting(
    userController: UserController,
    emailController: EmailController,
    passwordController: PasswordController,
    tokenController: TokenController
) {
    route("/api/") {
        post("addUser") {
            val newUser = call.receive<User>()
            val isNewUserAlreadyExist = userController.getUserByEmail(newUser.email)

            if (isNewUserAlreadyExist == null) {
                val password = passwordController.generatePassword()
                val hash = Bcrypt.hash(password, 5)

                userController.createUser(newUser, hash)
                emailController.sendEmail(newUser.firstName, newUser.email, password)
            } else {
                call.respondText(
                    """
                    { 
                        "description": "User is exist already"
                    }
                    """.trimIndent(),
                    ContentType.parse("application/json"),
                    HttpStatusCode.BadRequest
                )
            }
        }

        post("login") {
            val authUser = call.receive<LoginUser>()
            val user = userController.getUserByEmail(authUser.email)

            user?.let {
                val verify = Bcrypt.verify(authUser.password, user.password)
                if (verify) {
                    val id = user.id

                    val tokenPair = tokenController.generateTokenPair(id)
                    call.respond(tokenPair)
                }
            } ?: call.respond(HttpStatusCode.Unauthorized)
        }

        post("refresh") {
            val oldRefreshToken = call.receive<RefreshToken>().refreshToken
            val token = transaction {
                Tokens.select { Tokens.refreshToken eq oldRefreshToken }
                    .mapNotNull { it.toToken() }.singleOrNull()
            }
            val currentTime = System.currentTimeMillis()

            if (token != null && token.expiresAt > currentTime) {
                val tokenPair = tokenController.generateTokenPair(token.userId, true)

                transaction {
                    Tokens.update({ Tokens.refreshToken eq oldRefreshToken }) {
                        it[this.token] = tokenPair.accessToken
                        it[refreshToken] = tokenPair.refreshToken
                        it[expiresAt] = currentTime.withOffset(Duration.ofDays(Config.refreshLifetime))
                    }
                }

                call.respond(tokenPair)
            } else
                call.respondText(
                    """
                    { 
                        "description": "Invalid token"
                    }
                    """.trimIndent(),
                    ContentType.parse("application/json"),
                    HttpStatusCode.BadRequest
                )
        }

        authenticate("access") {
            get("user") {
                val user = userController.getUserByToken(getToken()!!)
                call.respond(user)
            }
        }
    }
}

fun PipelineContext<*, ApplicationCall>.getToken() =
    call.request.headers["Authorization"]?.removePrefix("Bearer ")
