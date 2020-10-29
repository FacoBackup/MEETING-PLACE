package br.meetingplace.management.services.community.dependencies.factory

import br.meetingplace.data.community.CommunityData
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteCommunity
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.community.Community

class CommunityFactory private constructor(): ReadWriteUser, ReadWriteLoggedUser, ReadWriteCommunity, Verify{

    companion object{
        private val Class = CommunityFactory()
        fun getClass () = Class
    }

    fun create(data: CommunityData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)

        if (verifyLoggedUser(user)){
            val newCommunity = Community.getCommunity()
            val id = data.name.replace("\\s".toRegex(),"").toLowerCase()
            newCommunity.startCommunity(data.name, id, data.about, loggedUser)
            user.social.updateModeratorIn(id,false)
            writeCommunity(newCommunity, id)
            writeUser(user, user.getEmail())
        }
    }
    fun delete(data: br.meetingplace.data.Data){
        // TODO: 10/29/20
    }
}