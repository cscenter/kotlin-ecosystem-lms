package org.jetbrains.edu.modules

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.resources.Resources
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.jetbrains.edu.db.*
import org.jetbrains.edu.model.*
import org.jetbrains.edu.resources.Courses
import org.jetbrains.edu.resources.Health
import org.jetbrains.edu.resources.Tasks
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.edu.db.Tasks as TasksTable

fun Application.tasksApi() {
    install(Resources)
    storage()

    routing {
        get<Health> {
            call.respond("Task Api is alive")
        }

        get<Tasks> {
            val count = transaction {
                TasksTable.selectAll().count()
            }
            call.respond(count)
        }

        post<Tasks.New> {
            val task = call.receive<Task>()

            val id = transaction {
                insertTask(task)
            }

            call.respond(HttpStatusCode.Created, id)
        }

        get<Tasks.Id> { id ->
            val task = transaction {
                getTaskById(id.id)
            }
            if (task != null) {
                call.respond(task)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        delete<Tasks.Id> { id ->
            transaction {
                TasksTable.deleteIgnoreWhere { TasksTable.id eq id.id }
            }
            call.respond(HttpStatusCode.OK)
        }

        get<Courses> {
            val courses = transaction {
                getAllCourses()
            }
            call.respond(courses)
        }

        get<Courses.Id.Assignments> { id ->
            val assignments = transaction {
                getCourseAssignments(id.parent.id)
            }
            call.respond(assignments)
        }
    }
}
