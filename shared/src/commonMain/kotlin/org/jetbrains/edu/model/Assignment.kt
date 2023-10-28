package org.jetbrains.edu.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Assignment(
    val softDeadline: LocalDate?,
    val hardDeadline: LocalDate?
)
