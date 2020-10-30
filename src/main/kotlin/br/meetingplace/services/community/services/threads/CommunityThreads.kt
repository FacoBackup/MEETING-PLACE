package br.meetingplace.services.community.services.threads

import br.meetingplace.services.community.data.Report

class CommunityThreads private constructor(): CommunityThreadsInterface{

    companion object{
        private val Class = CommunityThreads()
        fun getClass() = Class
    }

    private val threadsInValidation= mutableListOf<String>()
    private val approvedThreads = mutableListOf<String>()
    private val reportedThreads = mutableListOf<Report>()
    private val idReports = mutableListOf<String>()

    //GETTERS
    override fun getIdThreads() = approvedThreads
    override fun getThreadsInValidation ()= threadsInValidation
    override fun getReportedThreads () = reportedThreads
    override fun getIdReports() = idReports
    //GETTERS

    override fun checkThreadApproval(id: String): Boolean{
        return id in approvedThreads
    }
    override fun removeApprovedThread(thread: String){
        if(thread in approvedThreads)
            approvedThreads.remove(thread)
    }
    override fun updateThreadsInValidation(thread: String, approve: Boolean?){
        when(approve){
            true->{//APPROVE
                approvedThreads.add(thread)
                if(thread in threadsInValidation)
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

    override fun updateReport(data: Report, delete: Boolean){
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