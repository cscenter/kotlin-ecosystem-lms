package org.jetbrains.edu.modules

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.request.receive
import io.ktor.server.resources.Resources
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import org.jetbrains.edu.TaskManager
import org.jetbrains.edu.exceptins.NoSuchTask
import org.jetbrains.edu.data.Task
import org.jetbrains.edu.modules.Api.Tasks

fun Application.configureApi() {
    val taskManager: TaskManager = TaskManager.get()

    install(Resources)
    routing {
        get<Tasks> {
            call.respond(taskManager.count())
        }
        post<Tasks.New> {
            val task = call.receive<Task>()
            val id = taskManager.add(task)
            call.respond(HttpStatusCode.Created, "Task created with id $id")
        }
        get<Tasks.Id> { id ->
            val task = taskManager.get(id.id) ?: throw NoSuchTask(id.id)
            call.respond(task)
        }
        put<Tasks.Id> { id ->
            val newTask = call.receive<Task>()
            if (!taskManager.update(id.id, newTask)) throw NoSuchTask(id.id)
            call.respond(HttpStatusCode.OK)
        }
        delete<Tasks.Id> { id ->
            if (!taskManager.delete(id.id)) throw NoSuchTask(id.id)
            call.respond(HttpStatusCode.OK)
        }
    }
}
