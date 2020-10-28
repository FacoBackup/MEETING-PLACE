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

    //GETTERS
    fun getApprovedGroups() = approvedGroups
    fun getGroupsInValidation ()= groupsInValidation
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
                approvedGroups.add(group)
                if(group in groupsInValidation)
                    groupsInValidation.remove(group)
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
}