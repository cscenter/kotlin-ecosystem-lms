package org.jetbrains.edu

import io.ktor.server.application.*
import org.jetbrains.edu.exceptions.configureStatusPages
import org.jetbrains.edu.modules.*

fun Application.module() {
    configureContentNegotiation()
    configureStatusPages()
    tasksApi()
}
