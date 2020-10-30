package br.meetingplace.management.services.search.dependecies.user

import br.meetingplace.data.Data
import br.meetingplace.services.entitie.User

class UserSearch private constructor(): UserSearchInterface{

    companion object{
        private val Class = UserSearch()
        fun getClass() = Class
    }

    override fun searchUser(data: Data): List<User> {
        TODO("Not yet implemented")
    }
}