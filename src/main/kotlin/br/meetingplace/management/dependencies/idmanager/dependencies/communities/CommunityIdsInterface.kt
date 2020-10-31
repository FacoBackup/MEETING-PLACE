package br.meetingplace.management.dependencies.idmanager.dependencies.communities

interface CommunityIdsInterface {

    fun getCommunityId(communityName: String): String
    fun getCommunityGroupId(communityName: String, groupName: String): String
}