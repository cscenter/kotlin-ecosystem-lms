package org.jetbrains.edu.exceptions

class NoSuchTask(id: Long) : RuntimeException("No task with id $id")

class BrokenTask(id: Long, error: String) : RuntimeException("Task with id $id is broken: $error")