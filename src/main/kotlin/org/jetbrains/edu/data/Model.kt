package org.jetbrains.edu.data

import kotlinx.serialization.Serializable

enum class Language {
    NATURAL,
    KOTLIN,
    JAVA,
    CPP,
    PYTHON,
    HASKELL,
    RUST,
    NIM,
    OTHER;
}

@Serializable
data class Task(val description: String, val language: Language)
