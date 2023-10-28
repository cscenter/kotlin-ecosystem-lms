package org.jetbrains.edu.modules

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
fun Application.configureContentNegotiation() {
    install(ContentNegotiation) {
        json(Json {
            encodeDefaults = true
            explicitNulls = true
            prettyPrint = false
        })
    }
}
