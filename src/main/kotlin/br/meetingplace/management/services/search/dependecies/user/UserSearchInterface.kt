package br.meetingplace.management.services.search.dependecies.user

import br.meetingplace.data.Data
import br.meetingplace.management.services.search.dependecies.data.SimplifiedUser
import br.meetingplace.services.entitie.User

interface UserSearchInterface {
    fun searchUser(data: Data): List<SimplifiedUser>
}