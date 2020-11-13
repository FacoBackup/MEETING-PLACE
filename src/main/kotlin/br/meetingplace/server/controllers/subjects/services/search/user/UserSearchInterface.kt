package br.meetingplace.server.controllers.subjects.services.search.user

import br.meetingplace.server.dto.SimpleOperator
import br.meetingplace.server.subjects.entities.SimplifiedUser

interface UserSearchInterface {
    fun searchUser(data: SimpleOperator): List<SimplifiedUser>
}