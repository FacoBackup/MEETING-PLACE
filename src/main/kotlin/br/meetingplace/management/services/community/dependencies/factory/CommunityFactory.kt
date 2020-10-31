package br.meetingplace.management.services.community.dependencies.factory

import br.meetingplace.data.community.CommunityData
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.verify.dependencies.Verify
import br.meetingplace.management.dependencies.ReadWriteController
import br.meetingplace.management.dependencies.readwrite.dependencies.community.ReadWriteCommunity
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteUser
import br.meetingplace.services.community.Community
import br.meetingplace.services.notification.Inbox

class CommunityFactory private constructor(): ReadWriteUser, ReadWriteLoggedUser, ReadWriteCommunity, Verify {
    private val iDs = IDsController.getClass()
    companion object{
        private val Class = CommunityFactory()
        fun getClass () = Class
    }

    fun create(data: CommunityData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(iDs.getCommunityId(data.name))

        lateinit var newCommunity: Community
        lateinit var id: String

        if (verifyLoggedUser(user) && !verifyCommunity(community)){
            newCommunity = Community.getCommunity()
            id = iDs.getCommunityId(data.name)
            newCommunity.startCommunity(data.name, id, data.about, loggedUser)
            user.updateModeratorIn(id,false)

            writeCommunity(newCommunity, id)
            writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
        }
    }

    fun delete(data: br.meetingplace.data.Data){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        lateinit var community: Community
        lateinit var notification: Inbox
        lateinit var mods: List<String>

        if (verifyLoggedUser(user)){
            community = readCommunity(data.ID)
            if(verifyCommunity(community)){
                when(community.getModerators().isEmpty()){
                    true-> ReadWriteController.getDeleteFileOperator().deleteCommunity(community)
                    false->{
                        notification = Inbox("${user.getUserName()} requested for a community deletion, if you approve the deletion you have to step-down from moderator.", "Community")
                        mods = community.getModerators()
                        for (i in mods.indices){
                            val moderator = readUser(mods[i])
                            if(verifyUser(moderator)){
                                moderator.updateInbox(notification)
                            }
                        }
                    }
                }
            }
        }
    }
}