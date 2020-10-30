package br.meetingplace.management.dependencies

import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.services.entitie.User
import java.util.*

interface IDs: ReadWriteLoggedUser {
    fun getGroupId(groupName: String, creator:String?): String{

        if(!creator.isNullOrBlank())
                return (groupName.replace("\\s".toRegex(),"") +"_" +(creator.replaceAfter("@", "")).removeSuffix("@")).toLowerCase()
        return ""
    }
    fun getCommunityId(communityName: String): String{
        return (communityName.replace("\\s".toRegex(),"")).toLowerCase()
    }
    fun getCommunityGroupId(communityName: String, groupName: String): String{
        return (communityName + groupName).toLowerCase()
    }
    fun getGroupSimpleId(id: String): String {
        return (id.replace("\\s".toRegex(),"")).toLowerCase()
    }

    fun simpleToStandardIdGroup(id: String, user: User): String{
        val memberIn = user.getMemberIn()
        val fixedId = getGroupSimpleId(id)

        for(i in memberIn.indices){
            if(standardToSimpleIdGroup(memberIn[i]) == fixedId){
                return memberIn[i]
            }
        }
        return ""
    }

    fun standardToSimpleIdGroup(id: String): String {
        return (id.replaceAfter("_","")).removeSuffix("_")
    }
    fun generateId(): String {
        return UUID.randomUUID().toString()
    }
    fun getChatId(firstId: String, secondId: String): String {
        return firstId.removeSuffix("@") + secondId.removeSuffix("@")
    }

}