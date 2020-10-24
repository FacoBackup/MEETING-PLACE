package br.meetingplace.management.core.chat.dependencies

import br.meetingplace.data.group.GroupData
import br.meetingplace.data.group.GroupOperationsData
import br.meetingplace.data.group.MemberInput
import br.meetingplace.management.core.operators.Generator
import br.meetingplace.management.core.operators.Verify
import br.meetingplace.management.core.operators.fileOperators.DeleteFile
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteGroup
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.group.Group
import br.meetingplace.services.group.Member
import br.meetingplace.services.notification.Inbox

abstract class Group: ReadWriteUser, ReadWriteLoggedUser, ReadWriteGroup, Verify, Generator {
    fun createGroup(data: GroupData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)

        if(verifyUser(user)){
            val newGroup = Group()

            newGroup.startGroup(data.name, data.about, loggedUser)
            user.social.updateMyGroups(newGroup.getGroupId(),false)
            writeGroup(newGroup, newGroup.getGroupId())
            writeUser(user, loggedUser)
        }
    } // CREATE

    fun readMyGroups(): MutableList<Group> {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)

        val myGroups = mutableListOf<Group>()
        if(verifyUser(user)){
            val myGroupsIds = user.social.getMyGroups()
            for (i in 0 until myGroupsIds.size){
                val group = readGroup(myGroupsIds[i])
                if(verifyGroup(group))
                    myGroups.add(readGroup(myGroupsIds[i]))
            }
            return myGroups
        }
        return myGroups
    } // READ

    fun readMemberIn(): MutableList<Group> {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)

        val memberIn = mutableListOf<Group>()
        if(verifyUser(user)){
            val groupsIds = user.social.getMemberIn()
            for (i in 0 until groupsIds.size){
                val group = readGroup(groupsIds[i])
                if(verifyGroup(group))
                    memberIn.add(readGroup(groupsIds[i]))
            }
            return memberIn
        }
        return memberIn
    } // READ

    fun addMember(data: MemberInput){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val external = readUser(data.externalMember)
        val receiver = readGroup(data.groupId)

        if(verifyUser(user) && verifyUser(external) && verifyGroup(receiver) && receiver.verifyMember(loggedUser) && !receiver.verifyMember(data.externalMember)){
            val toBeAdded = Member(data.externalMember, 0)
            val notification = Inbox("You're now a member of ${receiver.getNameGroup()}", "Group.")

            receiver.updateMember(toBeAdded, false)
            external.social.updateMemberIn(receiver.getGroupId(), false)
            external.social.updateInbox(notification)
            writeUser(external, external.getEmail())
            writeGroup(receiver, receiver.getGroupId())
        }

    } //UPDATE

    fun removeMember(data: MemberInput){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val external = readUser(data.externalMember)
        val receiver = readGroup(data.groupId)

        if(verifyUser(user) && verifyUser(external) && verifyGroup(receiver) && receiver.verifyMember(loggedUser) && !receiver.verifyMember(data.externalMember)){
            val toBeRemoved = Member(data.externalMember, 0)

            receiver.updateMember(toBeRemoved, true)
            external.social.updateMemberIn(receiver.getGroupId(), true)
            writeUser(external, external.getEmail())
            writeGroup(receiver, receiver.getGroupId())
        }
    } //UPDATE

    fun deleteGroup(data: GroupOperationsData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val receiver = readGroup(data.idGroup)

        if(verifyUser(user) && verifyGroup(receiver) && receiver.getCreator() == loggedUser) {
            val members = receiver.getMembers()
            for(i in 0 until members.size){
                val member = readUser(members[i].userEmail)
                if(verifyUser(member)) {
                    member.social.updateMemberIn(receiver.getGroupId(),true)
                    writeUser(member, member.getEmail())
                }
            }

            user.social.updateMyGroups(receiver.getGroupId(),true)
            DeleteFile.getDeleteFileOperator().deleteGroup(receiver)
            writeUser(user, loggedUser)
        }//DELETE CHAT FROM GROUP
    } //DELETE
}