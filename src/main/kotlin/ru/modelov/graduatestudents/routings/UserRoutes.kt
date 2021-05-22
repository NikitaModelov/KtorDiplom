package ru.modelov.graduatestudents.routings

import com.toxicbakery.bcrypt.Bcrypt
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import ru.modelov.graduatestudents.contoller.email.EmailController
import ru.modelov.graduatestudents.contoller.password.PasswordController
import ru.modelov.graduatestudents.contoller.token.TokenController
import ru.modelov.graduatestudents.contoller.user.UserController
import ru.modelov.graduatestudents.model.user.mongo.SecretUser
import ru.modelov.graduatestudents.model.user.mongo.User
import ru.modelov.graduatestudents.model.user.mongo.UserRules
import ru.modelov.service.auth.LoginUser
import ru.modelov.service.auth.RefreshToken
import ru.modelov.util.exception.NoRelevantRules

fun Route.userRouting(
    userController: UserController,
    emailController: EmailController,
    passwordController: PasswordController,
    tokenController: TokenController
) {
    route("/api/") {

        post("addUser") {
            val newUser = call.receive<User>()

            val password = passwordController.generatePassword()
            val hash = Bcrypt.hash(password, 5)

            if (!userController.isExistUserByEmail(newUser.email)) {
                userController.createUser(newUser, hash)
                emailController.sendEmail(newUser.firstName, newUser.email, password)

                call.respond(HttpStatusCode.OK)
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
                val verify = Bcrypt.verify(authUser.password, user.password!!)
                if (verify) {
                    val id = user._id

                    val tokenPair = tokenController.generateTokenPair(id!!)
                    call.respond(HttpStatusCode.Accepted, tokenPair)
                }
            } ?: call.respond(HttpStatusCode.Unauthorized)
        }

        post("refresh") {
            val oldRefreshToken = call.receive<RefreshToken>().refreshToken
            val token = tokenController.getRefreshToken(oldRefreshToken)
            val currentTime = System.currentTimeMillis()

            if (token != null && token.expiresAt > currentTime) {
                val tokenPair = tokenController.generateTokenPair(token.userId, true)

                tokenController.updateToken(tokenPair, oldRefreshToken)

                call.respond(tokenPair)
            } else
                call.respondText(
                    """
                    { 
                        "description": "Invalid token"
                    }
                    """.trimIndent(),
                    ContentType.parse("application/json"),
                    HttpStatusCode.Unauthorized
                )
        }

        authenticate("access") {
            get("user") {
                val user = userController.getUserByTokenMongo(getToken()!!)
                user?.let {
                    call.respond(HttpStatusCode.OK, user)
                } ?: call.respondText(
                    """
                    { 
                        "description": "User not found"
                    }
                    """.trimIndent(),
                    ContentType.parse("application/json"),
                    HttpStatusCode.NotFound
                )
            }
            post("updateUser") {
                val updateUser = call.receive<SecretUser>()
                userController.updateUser(updateUser, getToken()!!)

                call.respond(HttpStatusCode.OK)
            }
            post("updateRules") {
                val updateUser = call.receive<UserRules>()
                try {
                    userController.updateUserRule(updateUser, getToken()!!)
                } catch (e: NoRelevantRules) {
                    return@post call.respondText(
                        """
                    { 
                        "message": "No relevant rules available"
                    }
                    """.trimIndent(),
                        ContentType.parse("application/json"),
                        HttpStatusCode.Locked
                    )
                }

                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

fun PipelineContext<*, ApplicationCall>.getToken() =
    call.request.headers["Authorization"]?.removePrefix("Bearer ")
