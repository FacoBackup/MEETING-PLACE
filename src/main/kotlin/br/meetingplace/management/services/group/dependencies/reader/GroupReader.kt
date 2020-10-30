package br.meetingplace.management.services.group.dependencies.reader

import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteGroup
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.group.Group

class GroupReader private constructor(): GroupReaderInterface, ReadWriteGroup, ReadWriteUser, ReadWriteLoggedUser, Verify {
    companion object{
        private val Class = GroupReader()
        fun getClass () =Class
    }

    override fun readMyGroups(): MutableList<Group> {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)

        val myGroups = mutableListOf<Group>()
        if(verifyLoggedUser(user)){
            val myGroupsIds = user.getMyGroups()
            for (i in myGroupsIds.indices){
                val group = readGroup(myGroupsIds[i])
                if(verifyGroup(group))
                    myGroups.add(readGroup(myGroupsIds[i]))
            }
            return myGroups
        }
        return myGroups
    } // READ

    override fun readMemberIn(): MutableList<Group> {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)

        val memberIn = mutableListOf<Group>()
        if(verifyLoggedUser(user)){
            val groupsIds = user.getMemberIn()
            for (i in groupsIds.indices){
                val group = readGroup(groupsIds[i])
                if(verifyGroup(group))
                    memberIn.add(readGroup(groupsIds[i]))
            }
            return memberIn
        }
        return memberIn
    } // READ

}