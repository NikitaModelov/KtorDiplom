package ru.modelov.graduatestudents.routings

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.modelov.graduatestudents.contoller.GraduateStudentsController

fun Route.graduateStudentRouting(controller: GraduateStudentsController) {
    route("/graduate_student") {
        get {
            val graduateStudent = controller.getAll() ?: return@get call.respondText(
                text = "Error request",
                status = HttpStatusCode.BadRequest
            )

            call.respond(graduateStudent)
        }
        get("{id}") {
            val id = call.parameters["id"]?.toLong() ?: return@get call.respondText(
                text = "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            val graduateStudent = controller.getById(id) ?: return@get call.respondText(
                text = "No graduate student with id $id",
                status = HttpStatusCode.BadRequest
            )

            call.respond(graduateStudent)
        }
    }
}