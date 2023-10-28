package org.jetbrains.edu.db

import org.jetbrains.edu.model.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*

fun Transaction.insertTask(task: Task) =
    when (task) {
        is ProgrammingTask -> insertProgrammingTask(task)
        is Choice -> insertChoiceQuestion(task)
        is OpenQuestion -> insertOpenQuestion(task)
        is Quiz -> insertQuiz(task)
    }


fun Transaction.insertProgrammingTask(task: ProgrammingTask) =
    Tasks.insertAndGetId {
        it[text] = task.description
        it[type] = TaskType.PROGRAMMING
        it[language] = task.language
        it[submit] = task.submit
    }


fun Transaction.insertChoiceQuestion(task: Choice): EntityID<Long> {
    val questionId = Tasks.insertAndGetId {
        it[text] = task.description
        it[type] = TaskType.CHOICE
    }

    val answers = task.answers.toSet()

    Answers.batchInsert(answers) {
        this[Answers.questionId] = questionId.value
        this[Answers.text] = task.options[it]
        this[Answers.correct] = it in answers
    }

    return questionId
}

fun Transaction.insertOpenQuestion(task: OpenQuestion) =
    Tasks.insertAndGetId {
        it[text] = task.description
        it[type] = TaskType.OPEN_QUESTION
    }


fun Transaction.insertQuiz(task: Quiz): EntityID<Long> {
    val quizId = Tasks.insertAndGetId {
        it[text] = task.description
        it[type] = TaskType.QUIZ
    }

    val questionIds = task.questions.map { insertTask(it) }

    QuizQuestions.batchInsert(questionIds) {
        this[QuizQuestions.quizId] = quizId.value
        this[QuizQuestions.questionId] = it.value
    }

    return quizId
}

fun Transaction.getTaskById(id: Long): Task? =
    Tasks.select { Tasks.id eq id }.singleOrNull()?.let {
        when (it[Tasks.type]) {
            TaskType.PROGRAMMING ->
                ProgrammingTask(
                    it[Tasks.text],
                    it[Tasks.language] ?: return null,
                    it[Tasks.submit] ?: return null
                )


            TaskType.CHOICE -> {
                val questionId = it[Tasks.id]
                val options = mutableListOf<String>()
                val answers = mutableListOf<Int>()
                Answers.select { Answers.questionId eq questionId.value }.forEach {
                    options.add(it[Answers.text])
                    if (it[Answers.correct]) {
                        answers.add(options.size - 1)
                    }
                }
                Choice(
                    it[Tasks.text],
                    options,
                    answers
                )
            }

            TaskType.OPEN_QUESTION -> {
                OpenQuestion(
                    it[Tasks.text]
                )
            }

            TaskType.QUIZ -> {
                val quizId = it[Tasks.id]
                val questions = mutableListOf<Question>()
                QuizQuestions.select { QuizQuestions.quizId eq quizId.value }.forEach {
                    val question = getTaskById(it[QuizQuestions.questionId]) as? Question
                    question?.let { questions.add(it) }
                }
                Quiz(
                    it[Tasks.text],
                    questions
                )
            }
        }
    }
