package org.jetbrains.edu.modules

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.jetbrains.edu.exceptins.NoSuchTask

inline fun <reified E : Throwable> StatusPagesConfig.wrapper(code: HttpStatusCode) =
    exception<E> { call, cause -> call.respond(code, "${code.value}${cause.message?.let { ": $it" } ?: ""}") }

fun Application.configureStatusPages() {
    install(StatusPages) {
        wrapper<NoSuchTask>(HttpStatusCode.NotFound)
        wrapper<Throwable>(HttpStatusCode.InternalServerError)
    }
}
