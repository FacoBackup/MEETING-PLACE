package br.meetingplace.management.dependencies.idmanager.dependencies.groups

import br.meetingplace.services.entitie.User

class GroupIds private constructor(): GroupIdsInterface{

    companion object{
        private val Class = GroupIds()
        fun getClass() = Class
    }

    override fun getGroupId(groupName: String, creator:String?): String{

        if(!creator.isNullOrBlank())
            return (groupName.replace("\\s".toRegex(),"") +"_" +(creator.replaceAfter("@", "")).removeSuffix("@")).toLowerCase()
        return ""
    }

    override fun getGroupSimpleId(id: String): String {
        return (id.replace("\\s".toRegex(),"")).toLowerCase()
    }

    override fun simpleToStandardIdGroup(id: String, user: User): String{
        val memberIn = user.getMemberIn()
        val fixedId = getGroupSimpleId(id)

        for(i in memberIn.indices){
            if(standardToSimpleIdGroup(memberIn[i]) == fixedId){
                return memberIn[i]
            }
        }
        return ""
    }
    override fun standardToSimpleIdGroup(id: String): String {
        return (id.replaceAfter("_","")).removeSuffix("_")
    }
}