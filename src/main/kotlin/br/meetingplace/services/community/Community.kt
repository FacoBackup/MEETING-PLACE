package br.meetingplace.services.community

import br.meetingplace.services.community.communityServices.CommunityGroups
import br.meetingplace.services.community.communityServices.CommunityThreads
import br.meetingplace.data.community.Report
import br.meetingplace.services.thread.MainThread

class Community{
    private var name= "" // THE NAME IS THE IDENTIFIER
    private var id = ""
    private var about= ""
    private val followers = mutableListOf<String>()
    private val moderators = mutableListOf<String>()
    private val threads = CommunityThreads.getThreads()
    private val groups = CommunityGroups.getGroups()

    fun startCommunity(name: String, id: String, about: String, creator: String){
        if(this.name == "" && moderators.isEmpty()){
            this.name = name
            this.about = about
            this.id = id
            moderators.add(creator)
        }
    }

    fun updateFollower(userEmail: String, remove:Boolean){
        when (remove){
            true->{
                if(userEmail in followers){
                    followers.remove(userEmail)
                }
            }
            false->{
                if(userEmail !in followers){
                    followers.add(userEmail)
                }
            }
        }
    }
    fun updateThreadRequest(thread: MainThread, requester: String,remove: Boolean){
        when (remove){
            true->{

            }
            false-> {

            }
        }
    }
    /*
    fun updateApprovedThread(thread: MainThread, requester: String,remove: Boolean){
        when (remove){
            true->{
                if(requester == thread.getCreator() || requester in moderators)
                    threads.remove(thread.getId())
            }
            false-> {
                if(requester in moderators)
                    threads.add(thread.getId())
            }
        }
    }
    /*
    fun updateModerator(userEmail: String, remove:Boolean, requester: String){
        when (remove){
            true->{
                //if(userEmail in moderators)
                 //   followers.remove(userEmail)
            }
            false->{
                if(userEmail !in moderators){
                    moderators.add(userEmail)
                }
            }
        }
    }//NEEDS WORK HERE
     */
    fun addReport(report: Report){
        if(report.reportId !in reportIds && !report.finished && report.creator in followers &&
            (report.idService in threads || report.idService in groups || report.idService in followers)){

            reports.add(report)
            reportIds.add(report.reportId)
        }
    }

    fun removeReport(reportId: String, requester: String){
        if(reportId in reportIds){
            val report = reports[getReportIndex(reportId)]
            if(report.creator == requester || requester in moderators){
                reportIds.remove(reportId)
                reports.remove(report)
            }
        }
    }

    fun updateReportStatus(reportId: String, finished: Boolean, requester:String ){
        if(reportId in reportIds){
            val report = reports[getReportIndex(reportId)]
            if(report.creator == requester || requester in moderators){
                report.finished = finished
            }
        }
    }

    private fun getReportIndex(reportId: String): Int {
        return if(reportId in reportIds){
            for(i in 0 until reportIds.size){
                if(reportId == reportIds[i])
                    return i
            }
            -1
        } else -1
    }

     */
}