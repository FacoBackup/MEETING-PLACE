package br.meetingplace.server.controllers.dependencies.readwrite.group

import br.meetingplace.server.subjects.services.groups.Group

interface GroupRWInterface {
    fun read(id: String): Group?
    fun write(data: Group)
    fun delete(data: Group)
}