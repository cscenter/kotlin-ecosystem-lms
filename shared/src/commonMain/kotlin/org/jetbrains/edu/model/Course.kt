package org.jetbrains.edu.model

import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val description: CourseDescription,
    val assignments: List<Assignment>,
    val materials: List<CourseMaterial>
)

@Serializable
data class CourseMaterial(
    val text: String
)

@Serializable
data class CourseDescription(
    val name: String,
    val description: String
)
