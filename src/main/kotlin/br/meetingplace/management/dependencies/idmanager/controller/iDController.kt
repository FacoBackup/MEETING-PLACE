package br.meetingplace.management.dependencies.idmanager.controller

import br.meetingplace.management.dependencies.idmanager.dependencies.chat.ChatIds
import br.meetingplace.management.dependencies.idmanager.dependencies.chat.ChatIdsInterface
import br.meetingplace.management.dependencies.idmanager.dependencies.communities.CommunityIds
import br.meetingplace.management.dependencies.idmanager.dependencies.communities.CommunityIdsInterface
import br.meetingplace.management.dependencies.idmanager.dependencies.groups.GroupIds
import br.meetingplace.management.dependencies.idmanager.dependencies.groups.GroupIdsInterface
import br.meetingplace.management.dependencies.idmanager.dependencies.users.UserIds
import br.meetingplace.management.dependencies.idmanager.dependencies.users.UserIdsInterface
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteLoggedUser
import br.meetingplace.services.entitie.User
import java.util.*

class IDsController private constructor(): ReadWriteLoggedUser, ChatIdsInterface, GroupIdsInterface, UserIdsInterface, CommunityIdsInterface{

    private val community = CommunityIds.getClass()
    private val user = UserIds.getClass()
    private val group = GroupIds.getClass()
    private val chat = ChatIds.getClass()

    companion object{
        private val Class = IDsController()
        fun getClass() = Class
    }

    //UNIVERSAL
    fun generateId(): String {
        return UUID.randomUUID().toString()
    }

    //COMMUNITY
    override fun getCommunityId(communityName: String): String{
        return community.getCommunityId(communityName)
    }
    override fun getCommunityGroupId(communityName: String, groupName: String): String{
        return community.getCommunityGroupId(communityName, groupName)
    }

    //CHAT
    override fun getChatId(firstId: String, secondId: String): String {
        return chat.getChatId(firstId, secondId)
    }

    //GROUP
    override fun getGroupId(groupName: String, creator:String?): String{
        return group.getGroupId(groupName,creator)
    }
    override fun getGroupSimpleId(id: String): String {
        return group.getGroupSimpleId(id)
    }
    override fun simpleToStandardIdGroup(id: String, user: User): String{
        return group.simpleToStandardIdGroup(id,user)
    }
    override fun standardToSimpleIdGroup(id: String): String {
        return group.standardToSimpleIdGroup(id)
    }

    //USER
    override fun fixUserName(name: String): String{
        return user.fixUserName(name)
    }
    override fun attachNameToEmail(name: String, email: String): String{
        return user.attachNameToEmail(name, email)
    }
    override fun getEmailByAttachedNameToEmail(attached: String): String{
        return user.getEmailByAttachedNameToEmail(attached)
    }
    override fun getNameByAttachedNameToEmail(attached: String): String{
        return user.getNameByAttachedNameToEmail(attached)
    }
}