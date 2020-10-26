package br.meetingplace.services.community.communityServices

import br.meetingplace.services.community.data.Report
import br.meetingplace.services.thread.MainThread

class CommunityThreads private constructor(){
    companion object{
        private val companion = CommunityThreads()
        fun getThreads() = companion
    }
    private val threadsInValidation= mutableListOf<String>()
    private val approvedThreads = mutableListOf<String>()
    private val reportedThreads = mutableListOf<Report>()
    private val idReports = mutableListOf<String>()

    //GETTERS
    fun getIdThreads() = approvedThreads
    fun getThreadsInValidation ()= threadsInValidation
    fun getReportedThreads () = reportedThreads
    fun getIdReports() = idReports
    //GETTERS

    fun checkThreadApproval(id: String): Boolean{
        return id in approvedThreads
    }
    fun removeApprovedThread(thread: String){
        if(thread in approvedThreads)
            approvedThreads.remove(thread)
    }
    fun updateThreadsInValidation(thread: String, approve: Boolean?){
        when(approve){
            true->{//APPROVE
                approvedThreads.add(thread)
                threadsInValidation.remove(thread)
            }
            false->{ // DELETE
                if(thread in threadsInValidation)
                    threadsInValidation.remove(thread)
            }
            null->{ //ADD
                println(thread)
                println(thread !in threadsInValidation)
                if(thread !in threadsInValidation && thread != "")
                    threadsInValidation.add(thread)
            }
        }
    }

    fun updateReport(data: Report,delete: Boolean){
        when(delete){
            true->{
                if(data.reportId in idReports){
                    reportedThreads.remove(data)
                    idReports.remove(data.reportId)
                }

            }
            false->{
                if(data.reportId !in idReports && data.idService in approvedThreads){
                    reportedThreads.add(data)
                    idReports.add(data.reportId)
                }

            }
        }
    }
}