package org.jetbrains.edu.modules

import io.ktor.resources.Resource

@Resource("/api")
class Api {
    @Resource("/tasks")
    class Tasks(val parent: Api = Api()) {
        @Resource("new")
        class New(val parent: Tasks = Tasks())

        @Resource("{id}")
        class Id(val parent: Tasks = Tasks(), val id: Long) {
            @Resource("edit")
            class Edit(val parent: Id)
        }
    }
}
