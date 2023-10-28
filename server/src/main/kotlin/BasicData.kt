import kotlinx.datetime.LocalDate
import org.jetbrains.edu.db.*
import org.jetbrains.edu.model.*
import org.jetbrains.exposed.sql.transactions.transaction

fun basicData() {
    // Kotlin
    val kotlinCourse = CourseDescription("Kotlin", "Kotlin Programming Language")
    val kotlinQuiz = Quiz(
        "Kotlin Quiz #1", listOf(
            Choice("Kotlin keywords", listOf("listOf", "val", "var", "int"), listOf(1, 2)),
            Choice("Ryan Gosling in the end of Drive is", listOf("dead", "alive", "I dont know"), listOf(2)),
            OpenQuestion("Best feature of Kotlin in your opinion")
        )
    )
    val kotlinTask1 = ProgrammingTask("Hello world in Kotlin", Language.KOTLIN)
    val kotlinTask2 = ProgrammingTask("LMS in Kotlin", Language.KOTLIN)

    // Pop Culture
    val popCourse = CourseDescription("Pop Culture", "Watching series, chilling")
    val popQuiz =  Quiz(
        "Pop quiz", listOf(
        Choice("Name the teacher", listOf("Jonas", "Martha", "The Stranger", "Eva"), listOf(3)),
        Choice("Name the course", listOf("1899", "Dark", "True Detective"), listOf(1)),
        Choice("Color of the coursebook", listOf("Dark", "Blue", "Transparent"), listOf(0))
    ))
    val progTasks = listOf(
        ProgrammingTask("Type inference", Language.HASKELL),
        ProgrammingTask("Hello world", Language.PYTHON, ProgrammingTask.Submit.CODE),
    )

    transaction {
        val kotlinId = insertCourse(kotlinCourse).value
        val kMaterialId = insertMaterial(CourseMaterial("Something very interesting about Kotlin")).value
        attachMaterial(kotlinId, kMaterialId)
        val k1 = insertTask(kotlinQuiz).value
        val k2 = insertTask(kotlinTask1).value
        val k3 = insertTask(kotlinTask2).value
        attachAssignment(kotlinId, Assignment(null, LocalDate(2023, 11, 1)), listOf(k1, k2))
        attachAssignment(kotlinId, Assignment(LocalDate(2023, 12, 1), LocalDate(2023, 12, 31)), listOf(k3))


        val popId = insertCourse(popCourse).value
        val popQuizId = insertTask(popQuiz).value
        val popProgIds = progTasks.map { insertTask(it).value }
        attachAssignment(popId, Assignment(null, null), listOf(popQuizId))
        attachAssignment(popId, Assignment(null, null), popProgIds)

        commit()
    }
}