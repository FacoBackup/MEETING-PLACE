package br.meetingplace.management.core.chat.dependencies

import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteLoggedUser

interface IDs: ReadWriteLoggedUser{
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
}