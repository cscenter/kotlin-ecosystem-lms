package org.jetbrains.edu.db

import kotlinx.datetime.LocalDate
import org.jetbrains.edu.db.QuizQuestions.references
import org.jetbrains.edu.model.*
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.date

enum class TaskType {
    PROGRAMMING,
    CHOICE,
    OPEN_QUESTION,
    QUIZ
}

object Tasks : LongIdTable("tasks") {
    val text: Column<String> = text("description")
    val type = enumeration("task_type", TaskType::class)

    val language = enumeration("language", Language::class).nullable()
    val submit = enumeration("submit", ProgrammingTask.Submit::class).nullable()
}

object Answers : LongIdTable("question_answers") {
    val questionId = long("question_id").references(Tasks.id)
    val text: Column<String> = text("text")
    val correct = bool("correct")
}

object QuizQuestions : Table("quiz_questions") {
    val quizId = long("quiz_id").references(Tasks.id)
    val questionId = long("question_id").references(Tasks.id)
}

object Courses : LongIdTable("courses") {
    val name: Column<String> = text("name")
    val text: Column<String> = text("description")
}

object CourseAssignments : LongIdTable("course_assignments") {
    val courseId = long("course_id").references(Courses.id)
    val softDeadline = date("soft_deadline").nullable()
    val hardDeadline = date("hard_deadline").nullable()
}

object AssignmentTasks : Table("assignment_tasks") {
    val assignmentId = long("assignment_id").references(CourseAssignments.id)
    val taskId = long("task_id").references(Tasks.id)
}

object Materials : LongIdTable("materials") {
    val text: Column<String> = text("text")
}

object CourseMaterials : Table("course_materials") {
    val courseId = long("course_id").references(Courses.id)
    val materialId = long("material_id").references(Materials.id)
}
