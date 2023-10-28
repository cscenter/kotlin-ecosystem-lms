package org.jetbrains.edu.resources

import io.ktor.resources.Resource

@Resource("/health")
class Health


@Resource("/api")
class Api

@Resource("/tasks")
class Tasks(val parent: Api = Api()) {
    @Resource("new")
    class New(val parent: Tasks = Tasks())

    @Resource("{id}")
    class Id(val parent: Tasks = Tasks(), val id: Long)
}

@Resource("/courses")
class Courses(val parent: Api = Api()) {
    @Resource("new")
    class New(val parent: Courses = Courses())

    @Resource("{id}")
    class Id(val parent: Courses = Courses(), val id: Long) {
        @Resource("assignments")
        class Assignments(val parent: Id)
    }
}
