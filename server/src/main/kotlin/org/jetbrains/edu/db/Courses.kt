package org.jetbrains.edu.db

import kotlinx.serialization.Serializable
import org.jetbrains.edu.model.*
import org.jetbrains.exposed.sql.*

@Serializable
data class CourseWithId(val courseDescription: CourseDescription, val id: Long)

fun Transaction.getAllCourses() =
    Courses.selectAll().map {
        CourseWithId(
            CourseDescription(it[Courses.name], it[Courses.text]),
            it[Courses.id].value
        )
    }

@Serializable
data class AssignmentWithTasks(val assignment: Assignment, val tasks: List<Long>)

fun Transaction.getCourseAssignments(courseId: Long) =
    CourseAssignments
        .select {
            CourseAssignments.courseId eq courseId
        }
        .map { assignmentResult ->
            val assignment = Assignment(
                assignmentResult[CourseAssignments.softDeadline],
                assignmentResult[CourseAssignments.hardDeadline]
            )
            val taskIds = AssignmentTasks.select {
                AssignmentTasks.assignmentId eq assignmentResult[CourseAssignments.id].value
            }.map { it[AssignmentTasks.taskId] }
            AssignmentWithTasks(assignment, taskIds)
        }


fun Transaction.insertCourse(courseDescription: CourseDescription) =
    Courses.insertAndGetId {
        it[name] = courseDescription.name
        it[text] = courseDescription.description
    }

fun Transaction.insertMaterial(material: CourseMaterial) =
    Materials.insertAndGetId {
        it[text] = material.text
    }

fun Transaction.attachMaterial(courseId: Long, materialId: Long) =
    CourseMaterials.insert {
        it[CourseMaterials.courseId] = courseId
        it[CourseMaterials.materialId] = materialId
    }

fun Transaction.attachAssignment(courseId: Long, assignment: Assignment, tasks: List<Long>) {
    val assignmentId = CourseAssignments.insertAndGetId {
        it[CourseAssignments.courseId] = courseId
        it[CourseAssignments.softDeadline] = assignment.softDeadline
        it[CourseAssignments.hardDeadline] = assignment.hardDeadline
    }

    AssignmentTasks.batchInsert(tasks) {
        this[AssignmentTasks.assignmentId] = assignmentId.value
        this[AssignmentTasks.taskId] = it
    }
}
