package br.meetingplace.server.controllers.dependencies.readwrite.user

import br.meetingplace.server.subjects.entities.User

interface UserRWInterface {
    fun read(id: String): User?
    fun write(data: User)
    fun delete(data: User)
}