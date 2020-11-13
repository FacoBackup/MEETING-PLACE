package br.meetingplace.server.controllers.dependencies.id.communities

interface CommunityIdsInterface {

    fun getCommunityId(communityName: String): String
    fun getCommunityGroupId(communityName: String, groupName: String): String
}