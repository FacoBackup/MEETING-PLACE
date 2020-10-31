package br.meetingplace.management.dependencies.idmanager.dependencies.communities

class CommunityIds private constructor(): CommunityIdsInterface{

    companion object{
        private val Class = CommunityIds()
        fun getClass() = Class
    }

    override fun getCommunityId(communityName: String): String{
        return (communityName.replace("\\s".toRegex(),"")).toLowerCase()
    }
    override fun getCommunityGroupId(communityName: String, groupName: String): String{
        return (communityName + groupName).toLowerCase()
    }
}