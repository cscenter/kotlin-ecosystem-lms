package org.jetbrains.edu.teamporalanomaly

import org.jetbrains.edu.data.Language
import org.jetbrains.edu.data.Task
import kotlin.collections.filterNotNull
import kotlin.collections.getOrNull
import kotlin.collections.getOrNull

interface TaskManager {
    companion object {
        fun get(): TaskManager = DumbTaskManager()
    }

    fun count(): Long

    fun add(task: Task): Long

    fun get(id: Long): Task?

    fun update(id: Long, task: Task): Boolean

    fun delete(id: Long): Boolean
}

class DumbTaskManager : TaskManager {
    private val task0 = Task("How I spent last summer", Language.NATURAL)
    private val task1 = Task("BST in Kotlin", Language.KOTLIN)
    private val task2 = Task("BST in CPP", Language.CPP)
    private val task3 = Task("FCB", Language.CPP)
    private val task4 = Task("Android app", Language.KOTLIN)
    private val tasks: MutableList<Task?> = mutableListOf(task0, null, task1, task2, task3, task4)

    override fun count() = tasks.filterNotNull().size.toLong()

    override fun add(task: Task): Long {
        tasks.add(task)
        return tasks.size.toLong() - 1
    }

    override fun get(id: Long) = tasks.getOrNull(id.toInt())

    override fun update(id: Long, task: Task): Boolean {
        if (tasks.size <= id.toInt()) return false
        tasks[id.toInt()] = task
        return true
    }

    override fun delete(id: Long): Boolean {
        val task = tasks.getOrNull(id.toInt())
        if (task != null) tasks[id.toInt()] = null
        return task != null
    }
}
