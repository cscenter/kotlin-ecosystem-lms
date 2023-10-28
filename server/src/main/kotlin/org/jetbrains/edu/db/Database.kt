package org.jetbrains.edu.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

private fun createHikariDataSource(config: StorageConfig) = HikariDataSource(
    HikariConfig().apply {
        jdbcUrl = config.url
        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        username = config.user
        password = config.pass
        validate()
    }
)

fun Application.storage() {
    val config = getStorageConfig()

    val hikariDataSource = createHikariDataSource(config)

    Database.connect(hikariDataSource)

    transaction {
        SchemaUtils.createMissingTablesAndColumns(
            Tasks,
            Answers,
            QuizQuestions,
            Courses,
            CourseAssignments,
            AssignmentTasks,
            Materials,
            CourseMaterials
        )
    }
}
