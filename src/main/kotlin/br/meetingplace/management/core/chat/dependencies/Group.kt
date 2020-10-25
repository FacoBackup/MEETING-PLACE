package br.meetingplace.management.core.chat.dependencies

import br.meetingplace.data.group.GroupData
import br.meetingplace.data.group.GroupOperationsData
import br.meetingplace.data.group.MemberInput
import br.meetingplace.management.core.operators.Generator
import br.meetingplace.management.core.operators.Verify
import br.meetingplace.management.core.operators.fileOperators.DeleteFile
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteCommunity
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteGroup
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.group.Group
import br.meetingplace.services.group.Member
import br.meetingplace.services.notification.Inbox

abstract class Group: ReadWriteUser, ReadWriteLoggedUser, ReadWriteGroup, Verify, Generator, ReadWriteCommunity{

    private fun getGroupId(groupName: String): String{
        val loggedUser = readLoggedUser().email
        return (groupName.replace("\\s".toRegex(),"") +"_" +(loggedUser.replaceAfter("@", "")).removeSuffix("@")).toLowerCase()
    }

    fun createGroup(data: GroupData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)

        if(verifyLoggedUser(user) && data.groupName.isNotEmpty()){
            val newGroup = Group()
            var id = getGroupId(data.groupName)

            when(data.idCommunity.isNullOrBlank()){
                true->{
                    newGroup.startGroup(data.groupName, id,data.about, loggedUser)
                    user.social.updateMyGroups(newGroup.getGroupId(),false)
                    writeGroup(newGroup, newGroup.getGroupId())
                    writeUser(user, loggedUser)
                }
                false->{
                    val community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community)){
                        val communityMods = community.getModerators()
                        val notification = Inbox("$loggedUser wants to create a new group in ${community.getName()}.", "Community Group")

                        for(i in 0 until communityMods.size){
                            val mod = readUser(communityMods[i])
                            if (verifyUser(mod) && mod != user)
                                mod.social.updateInbox(notification)
                        }

                        id= id+"_"+data.idCommunity
                        newGroup.startGroup(data.groupName, id,data.about, loggedUser)
                        user.social.updateMyGroups(newGroup.getGroupId(),false)
                        community.groups.updateGroupsInValidation(newGroup.getGroupId(), null)

                        writeGroup(newGroup, newGroup.getGroupId())
                        writeUser(user, loggedUser)
                        writeCommunity(community, community.getId())
                    }
                }
            }
        }
    } // CREATE

    fun readMyGroups(): MutableList<Group> {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)

        val myGroups = mutableListOf<Group>()
        if(verifyLoggedUser(user)){
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
        if(verifyLoggedUser(user)){
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
        var id = data.groupName
        if(!data.idCommunity.isNullOrBlank())
            id= id+"_"+data.idCommunity
        val groupId = getGroupId(id)
        val receiver = readGroup(groupId)

        if(verifyLoggedUser(user) && verifyUser(external) && verifyGroup(receiver) && receiver.verifyMember(loggedUser) && !receiver.verifyMember(data.externalMember)){
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
        var id = data.groupName
        if(!data.idCommunity.isNullOrBlank())
            id= id+"_"+data.idCommunity
        val groupId = getGroupId(id)
        val receiver = readGroup(groupId)

        if(verifyLoggedUser(user) && verifyUser(external) && verifyGroup(receiver) && receiver.verifyMember(loggedUser) && !receiver.verifyMember(data.externalMember)){
            val toBeRemoved = Member(data.externalMember, 0)

            receiver.updateMember(toBeRemoved, true)
            external.social.updateMemberIn(receiver.getGroupId(), true)
            writeUser(external, external.getEmail())
            writeGroup(receiver, receiver.getGroupId())
        }
    } //UPDATE

    fun deleteGroup(data: GroupOperationsData){ //NEEDS WORK FOR COMMUNITY
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        var id = data.groupName
        if(!data.idCommunity.isNullOrBlank())
            id= id+"_"+data.idCommunity
        val groupId = getGroupId(id)
        val receiver = readGroup(groupId)

        if(verifyLoggedUser(user) && verifyGroup(receiver) && receiver.getCreator() == loggedUser) {
            val members = receiver.getMembers()
            for(i in 0 until members.size){
                val member = readUser(members[i].userEmail)
                if(verifyUser(member)) {
                    member.social.updateMemberIn(receiver.getGroupId(),true)
                    writeUser(member, member.getEmail())
                }
            }

            when(data.idCommunity.isNullOrBlank()){
                true->{
                    user.social.updateMyGroups(receiver.getGroupId(),true)
                    DeleteFile.getDeleteFileOperator().deleteGroup(receiver)
                    writeUser(user, loggedUser)
                }
                false->{
                    val community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && (loggedUser == receiver.getCreator() || loggedUser in community.getModerators())){

                       when(community.groups.checkGroupApproval(receiver.getGroupId())){
                           true->{
                               community.groups.removeApprovedGroup(receiver.getGroupId())
                               user.social.updateMyGroups(receiver.getGroupId(),true)
                               DeleteFile.getDeleteFileOperator().deleteGroup(receiver)
                               writeUser(user, loggedUser)
                               writeCommunity(community, community.getId())
                           }
                           false->{
                               community.groups.updateGroupsInValidation(receiver.getGroupId(), false)
                               user.social.updateMyGroups(receiver.getGroupId(),true)
                               DeleteFile.getDeleteFileOperator().deleteGroup(receiver)
                               writeUser(user, loggedUser)
                               writeCommunity(community, community.getId())
                           }
                       }

                    }
                }
            }

        }//DELETE CHAT FROM GROUP
    } //DELETE
}