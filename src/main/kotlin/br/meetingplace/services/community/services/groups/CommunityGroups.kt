package br.meetingplace.services.community.services.groups

class CommunityGroups private constructor(): CommunityGroupsInterface{

    companion object{
        private val Class = CommunityGroups()
        fun getClass () = Class
    }

    private val approvedGroups = mutableListOf<String>()
    private val groupsInValidation = mutableListOf<String>()

    override fun getApprovedGroups() = approvedGroups
    override fun getGroupsInValidation ()= groupsInValidation

    override fun checkGroupApproval(id: String): Boolean{
        return id in approvedGroups
    }

    override fun removeApprovedGroup(group: String){
        if(group in approvedGroups)
            approvedGroups.remove(group)
    }

    override fun updateGroupsInValidation(group: String,approve: Boolean?){
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