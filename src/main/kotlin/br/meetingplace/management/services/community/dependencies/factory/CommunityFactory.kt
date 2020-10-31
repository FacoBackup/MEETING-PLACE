package br.meetingplace.management.services.community.dependencies.factory

import br.meetingplace.data.community.CommunityData
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.services.community.Community
import br.meetingplace.services.notification.Inbox

class CommunityFactory private constructor(){
    private val iDs = IDsController.getClass()
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()

    companion object{
        private val Class = CommunityFactory()
        fun getClass () = Class
    }

    fun create(data: CommunityData){
        val loggedUser = rw.readLoggedUser().email
        val user = rw.readUser(loggedUser)
        val community = rw.readCommunity(iDs.getCommunityId(data.name))

        lateinit var newCommunity: Community
        lateinit var id: String

        if (verify.verifyUser(user) && !verify.verifyCommunity(community)){
            newCommunity = Community.getCommunity()
            id = iDs.getCommunityId(data.name)
            newCommunity.startCommunity(data.name, id, data.about, loggedUser)
            user.updateModeratorIn(id,false)

            rw.writeCommunity(newCommunity, id)
            rw.writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
        }
    }

    fun delete(data: br.meetingplace.data.Data){
        val loggedUser = rw.readLoggedUser().email
        val user = rw.readUser(loggedUser)
        lateinit var community: Community
        lateinit var notification: Inbox
        lateinit var mods: List<String>

        if (verify.verifyUser(user)){
            community = rw.readCommunity(data.ID)
            if(verify.verifyCommunity(community)){
                when(community.getModerators().isEmpty()){
                    true-> rw.deleteCommunity(community)
                    false->{
                        notification = Inbox("${user.getUserName()} requested for a community deletion, if you approve the deletion you have to step-down from moderator.", "Community")
                        mods = community.getModerators()
                        for (i in mods.indices){
                            val moderator = rw.readUser(mods[i])
                            if(verify.verifyUser(moderator)){
                                moderator.updateInbox(notification)
                            }
                        }
                    }
                }
            }
        }
    }
}