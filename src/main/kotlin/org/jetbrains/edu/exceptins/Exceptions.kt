package org.jetbrains.edu.exceptins

class NoSuchTask(id: Long) : RuntimeException("No task with id $id")
