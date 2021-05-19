package ru.modelov.graduatestudents.routings

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.modelov.graduatestudents.contoller.GraduateStudentsController
import ru.modelov.graduatestudents.contoller.GroupController

fun Route.graduateStudentRouting(
    graduateStudentsController: GraduateStudentsController,
    groupController: GroupController
) {
    route("/api/graduate_student") {
        authenticate("access") {
            get() {
                val year = call.request.queryParameters["year"] ?: return@get call.respondText(
                    text = "Missing or malformed year",
                    status = HttpStatusCode.BadRequest
                )

                val faculty = groupController.getFacultyByToken(getToken()!!)
                val graduateStudent = graduateStudentsController.getAll(year, faculty)


            call.respond(graduateStudent)
        }
        get("{id}") {
            val id = call.parameters["id"]?.toInt() ?: return@get call.respondText(
                text = "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            val graduateStudent = graduateStudentsController.getById(id) ?: return@get call.respondText(
                text = "No graduate student with id $id",
                status = HttpStatusCode.BadRequest
            )

                call.respond(graduateStudent)
            }
        }
    }
}
