package br.meetingplace.management.interfaces.community

import br.meetingplace.services.community.data.Report

interface CommunityServices {
    fun createThread(){

    }
    fun deleteThread(){

    }
    fun createGroup(){

    }
    fun deleteGroup(){

    }

    fun addReport(report: Report){

    }

    fun removeReport(reportId: String, requester: String){

    }
}