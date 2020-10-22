package br.meetingplace.management.interfaces.community

interface Moderators {
    fun seeReport(){

    }
    fun seeToBeApprovedThreads(){

    }
    fun seeToBeApprovedGroups(){

    }
    fun updateReportStatus(reportId: String, finished: Boolean, requester:String ){

    }
}