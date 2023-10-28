package org.jetbrains.edu.db

import io.ktor.server.application.*

data class StorageConfig(
    val host: String,
    val port: String,
    val user: String,
    val pass: String,
    val name: String,
    val prefix: String
) {
    val url: String
        get() = "$prefix://$host:$port/$name"
}

fun Application.getStorageConfig() =
    environment.config.let {
        StorageConfig(
            it.property("ktor.storage.host").getString(),
            it.property("ktor.storage.port").getString(),
            it.property("ktor.storage.user").getString(),
            it.property("ktor.storage.pass").getString(),
            it.property("ktor.storage.name").getString(),
            it.property("ktor.storage.prefix").getString()
        )
    }
