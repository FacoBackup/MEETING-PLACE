package br.meetingplace.services.community.communityServices

import br.meetingplace.data.community.Report

class CommunityThreads private constructor(){
    companion object{
        private val companion = CommunityThreads()
        fun getThreads() = companion
    }
    private val toBeApprovedThreads= mutableListOf<String>()
    private val threads = mutableListOf<String>()
    private val reportedThreads = mutableListOf<Report>()
    private val reportIds = mutableListOf<String>()
}