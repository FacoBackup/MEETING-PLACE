package br.meetingplace.management.services.search.dependecies.user

import br.meetingplace.data.Data
import br.meetingplace.services.entitie.User

interface UserSearchInterface {
    fun searchUser(data: Data): User?
}