package ru.modelov.graduatestudents.routings

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.modelov.graduatestudents.contoller.faculty.FacultyController
import ru.modelov.graduatestudents.contoller.user.GraduateStudentsController

fun Route.graduateStudentRouting(
    graduateStudentsController: GraduateStudentsController,
    facultyController: FacultyController
) {
    route("/api/graduate_student") {
        authenticate("access") {
            get {
                val year = call.request.queryParameters["year"] ?: return@get call.respondText(
                    text = "Missing or malformed year",
                    status = HttpStatusCode.BadRequest
                )

                val faculty = facultyController.getFacultyByToken(getToken()!!)
                faculty?.let {
                    val graduateStudent = graduateStudentsController.getAllGraduate(year, faculty)
                    call.respond(graduateStudent)
                }
        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                text = "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            val graduateStudent = graduateStudentsController.getUserById(id) ?: return@get call.respondText(
                text = "No graduate student with id: $id",
                status = HttpStatusCode.BadRequest
            )

                call.respond(graduateStudent)
            }
        }
    }
}
