package ru.modelov

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.Database
import ru.modelov.graduatestudents.contoller.GraduateStudentsController
import ru.modelov.graduatestudents.routings.graduateStudentRouting

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

/**
 * Please note that you can use any other name instead of *module*.
 * Also note that you can have more then one modules in your application.
 * */
@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    initDatabase()
    install(ContentNegotiation) {
        gson()
    }
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
        graduateStudentRouting(controller = GraduateStudentsController())
    }
}
