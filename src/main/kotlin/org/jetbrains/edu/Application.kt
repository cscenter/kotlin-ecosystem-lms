package org.jetbrains.edu

import io.ktor.server.application.*
import org.jetbrains.edu.plugins.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureHTTP()
    configureSerialization()
    configureRouting()
}
