package br.meetingplace.management.services.search.dependecies.user

import br.meetingplace.data.Data
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.entitie.User

class UserSearch private constructor(): UserSearchInterface, ReadWriteUser, ReadWriteLoggedUser, Verify{

    companion object{
        private val Class = UserSearch()
        fun getClass() = Class
    }

    override fun searchUser(data: Data): User?{
        val user = readUser(data.ID)
        return if(verifyUser(user))
            user
        else null
    }
}