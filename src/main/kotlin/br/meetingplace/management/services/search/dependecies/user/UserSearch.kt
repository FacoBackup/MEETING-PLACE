package br.meetingplace.management.services.search.dependecies.user

import br.meetingplace.data.Data
import br.meetingplace.management.dependencies.verify.dependencies.Verify
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteUser
import br.meetingplace.management.services.search.dependecies.type.SearchType
import br.meetingplace.services.entitie.User

class UserSearch private constructor(): UserSearchInterface, ReadWriteUser, ReadWriteLoggedUser, Verify {

    companion object{
        private val Class = UserSearch()
        fun getClass() = Class
    }

    override fun searchUser(data: Data): List<User>{
        when(verifyType(data)){
            SearchType.BY_NAME->{

            }
            SearchType.BY_EMAIL->{
                val user = readUser(data.ID)
                return if(verifyUser(user))
                    listOf(user)
                else listOf()
            }
        }
        return listOf()
    }

    private fun verifyType(data: Data): SearchType?{
        return if(data.ID.contains("@"))
            SearchType.BY_EMAIL
        else if (!data.ID.contains("@"))
            SearchType.BY_NAME
        else null
    }
}