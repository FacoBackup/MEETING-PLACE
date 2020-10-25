package br.meetingplace.management.core.community

import br.meetingplace.data.community.CommunityData
import br.meetingplace.data.community.ReportData
import br.meetingplace.data.user.Follower
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteGroup
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteThread
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteUser

class CommunityOperations: ReadWriteUser, ReadWriteLoggedUser, ReadWriteThread, ReadWriteGroup{
    private fun verifyReportType(data: ReportData): Int{
        val asGroup = readGroup(data.idService)
        val asThread = readThread(data.idService)

        return if(asGroup.getGroupId() == "" && asThread.getId() != ""){ //IS A THREAD
            0
        }
        else if(asGroup.getGroupId() != "" && asThread.getId() == ""){ // IS A GROUP
            1
        }
        else // IS NONE OF THE ABOVE
            -1
    }

    fun create(data: CommunityData){

    }

    fun createReport(data: ReportData){

    }

    fun follow(data: Follower){

    }

    fun unfollow(data: Follower){

    }

    fun createThread(){

    }

    fun deleteThread(){

    }

    fun approveThread(){

    }

    fun createGroup(){

    }

    fun deleteGroup(){

    }

    fun approveGroup(){

    }

}