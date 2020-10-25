package br.meetingplace.services.community.communityServices

import br.meetingplace.services.community.data.Report
import br.meetingplace.services.group.Group

class CommunityGroups private constructor() {
    companion object{
        private val companion = CommunityGroups()
        fun getGroups () = companion
    }
    private val approvedGroups = mutableListOf<String>() //OK
    private val groupsInValidation = mutableListOf<String>() //NEEDS WORK HERE
    private val reportedGroups = mutableListOf<Report>() // OK
    private val idReports = mutableListOf<String>() // OK

    //GETTERS
    fun getApprovedGroups() = approvedGroups
    fun getGroupsInValidation ()= groupsInValidation
    fun getReportedGroups () = reportedGroups
    fun getIdReports() = idReports
    //GETTERS

    fun checkGroupApproval(id: String): Boolean{
        return id in approvedGroups
    }

    fun removeApprovedGroup(group: String){
        if(group in approvedGroups)
            approvedGroups.remove(group)
    }

    fun updateGroupsInValidation(group: String,approve: Boolean?){
        when(approve){
            true->{//APPROVE
                if(group in groupsInValidation){
                    approvedGroups.add(group)
                    groupsInValidation.remove(group)
                }
            }
            false->{ // DELETE
                if(group in groupsInValidation)
                    groupsInValidation.remove(group)
            }
            null->{ //ADD
                if(group !in groupsInValidation && group != "")
                    groupsInValidation.add(group)
            }
        }
    }

    fun updateReport(data: Report,delete: Boolean){
        when(delete){
            true->{
                if(data.reportId in idReports){
                    reportedGroups.remove(data)
                    idReports.remove(data.reportId)
                }
            }
            false->{
                if(data.reportId !in idReports && data.idService in approvedGroups){
                    reportedGroups.add(data)
                    idReports.add(data.reportId)
                }
            }
        }
    }


}