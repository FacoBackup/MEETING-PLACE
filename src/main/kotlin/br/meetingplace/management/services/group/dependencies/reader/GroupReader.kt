package br.meetingplace.management.services.group.dependencies.reader

import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.services.group.Group

class GroupReader private constructor(): GroupReaderInterface{
    private val iDs = IDsController.getClass()
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()

    companion object{
        private val Class = GroupReader()
        fun getClass () =Class
    }

    override fun readMyGroups(): MutableList<Group> {
        val loggedUser = rw.readLoggedUser().email
        val user = rw.readUser(loggedUser)

        val myGroups = mutableListOf<Group>()
        if(verify.verifyUser(user)){
            val myGroupsIds = user.getMyGroups()
            for (i in myGroupsIds.indices){
                val group = rw.readGroup(myGroupsIds[i])
                if(verify.verifyGroup(group))
                    myGroups.add(rw.readGroup(myGroupsIds[i]))
            }
            return myGroups
        }
        return myGroups
    } // READ

    override fun readMemberIn(): MutableList<Group> {
        val loggedUser =rw.readLoggedUser().email
        val user = rw.readUser(loggedUser)

        val memberIn = mutableListOf<Group>()
        if(verify.verifyUser(user)){
            val groupsIds = user.getMemberIn()
            for (i in groupsIds.indices){
                val group = rw.readGroup(groupsIds[i])
                if(verify.verifyGroup(group))
                    memberIn.add(rw.readGroup(groupsIds[i]))
            }
            return memberIn
        }
        return memberIn
    } // READ

}