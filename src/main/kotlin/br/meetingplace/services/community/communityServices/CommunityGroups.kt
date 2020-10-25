package br.meetingplace.services.community.communityServices

import br.meetingplace.data.community.Report

class CommunityGroups private constructor() {
    companion object{
        private val companion = CommunityGroups()
        fun getGroups () = companion
    }
    private val toBeApprovedGroup= mutableListOf<String>()
    private val groups = mutableListOf<String>()
    private val reportedGroups = mutableListOf<Report>()
    private val reportIds = mutableListOf<String>()
}