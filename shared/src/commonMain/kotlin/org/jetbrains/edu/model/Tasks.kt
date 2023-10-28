package org.jetbrains.edu.model

import kotlinx.serialization.Serializable

@Serializable
sealed class Task {
    abstract val description: String
}

@Serializable
data class ProgrammingTask(
    override val description: String,
    val language: Language,
    val submit: Submit = Submit.REPO
) : Task() {
    enum class Submit {
        REPO,
        CODE
    }
}

@Serializable
sealed class Question : Task()

@Serializable
data class Choice(
    val text: String,
    val options: List<String>,
    val answers: List<Int>
) : Question() {
    override val description: String
        get() = "Choice question with ${options.size} choices: $text"
}

@Serializable
data class OpenQuestion(
    val text: String
) : Question() {
    override val description: String
        get() = "Open question: $text"
}

@Serializable
data class Quiz(
    override val description: String,
    val questions: List<Question>
) : Task()
