package br.meetingplace.servicies.community

import br.meetingplace.servicies.community.data.Report
import br.meetingplace.servicies.community.interfaces.Moderators

class Community: Moderators{
    private var name= "" // THE NAME IS THE IDENTIFIER
    private var about= ""
    private val approvedThreads = mutableListOf<String>()
    private val approvedGroups = mutableListOf<String>()
    private val followers = mutableListOf<String>()
    private val moderators = mutableListOf<String>()
    private val toBeApprovedGroup= mutableListOf<String>()
    private val toBeApprovedThreads= mutableListOf<String>()

    private val reports = mutableListOf<Report>()
    private val reportIds = mutableListOf<String>()

    fun addReport(report: Report){
        if(report.reportId !in reportIds && !report.finished && report.creator in followers &&
            (report.idService in approvedThreads || report.idService in approvedGroups || report.idService in followers)){

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
}