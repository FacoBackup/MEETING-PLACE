package br.meetingplace.management.chat.dependencies

import br.meetingplace.data.group.GroupData
import br.meetingplace.data.group.GroupOperationsData
import br.meetingplace.data.group.MemberInput
import br.meetingplace.management.dependencies.IDs
import br.meetingplace.management.dependencies.Generator
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.DeleteFile
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteCommunity
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteGroup
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.group.Group
import br.meetingplace.services.group.Member
import br.meetingplace.services.notification.Inbox

abstract class Group: ReadWriteUser, ReadWriteLoggedUser, ReadWriteGroup, Verify, Generator, ReadWriteCommunity, IDs {

    fun createGroup(data: GroupData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)

        if(verifyLoggedUser(user) && data.groupName.isNotEmpty()){
            val newGroup = Group()
            var id = getGroupId(data.groupName, loggedUser)

            when(data.idCommunity.isNullOrBlank()){
                true->{
                    newGroup.startGroup(data.groupName, id,data.about, loggedUser)
                    user.social.updateMyGroups(newGroup.getGroupId(),false)
                    writeGroup(newGroup, newGroup.getGroupId())
                    writeUser(user, loggedUser)
                }
                false->{
                    val community = readCommunity(getCommunityId(data.idCommunity))
                    if(verifyCommunity(community)){
                        val communityMods = community.getModerators()
                        val notification = Inbox("$loggedUser wants to create a new group in ${community.getName()}.", "Community Group")

                        for(i in 0 until communityMods.size){
                            val mod = readUser(communityMods[i])
                            if (verifyUser(mod) && mod != user)
                                mod.social.updateInbox(notification)
                        }

                        id= getCommunityGroupId(community.getId(), data.groupName)

                        newGroup.startGroup(data.groupName, id,data.about, loggedUser)
                        user.social.updateMyGroups(newGroup.getGroupId(),false)

                        if(loggedUser !in communityMods)
                            community.groups.updateGroupsInValidation(newGroup.getGroupId(), null)
                        else
                            community.groups.updateGroupsInValidation(newGroup.getGroupId(), true)

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

        if(verifyLoggedUser(user) && verifyUser(external)){
            val toBeAdded = Member(data.externalMember, 0)
            when(data.idCommunity.isNullOrBlank()){
                true->{
                    val groupId = getGroupId(data.groupName, data.creator)
                    val receiver = readGroup(groupId)
                    val notification = Inbox("You're now a member of ${receiver.getNameGroup()}", "Group.")
                    if(verifyUser(external) && verifyGroup(receiver) && receiver.verifyMember(loggedUser) && !receiver.verifyMember(data.externalMember))
                    receiver.updateMember(toBeAdded, false)
                    external.social.updateMemberIn(receiver.getGroupId(), false)
                    external.social.updateInbox(notification)
                    writeUser(external, external.getEmail())
                    writeGroup(receiver, receiver.getGroupId())
                }
                false->{
                    val groupId = getCommunityGroupId(data.idCommunity,getGroupId(data.groupName, data.creator))
                    val receiver = readGroup(groupId)
                    val community = readCommunity(getCommunityId(data.idCommunity))
                    val notification = Inbox("You're now a member of ${receiver.getNameGroup()}", "Group.")
                    if(verifyCommunity(community) && (loggedUser == receiver.getCreator() || loggedUser in community.getModerators())){

                        receiver.updateMember(toBeAdded, false)
                        external.social.updateMemberIn(receiver.getGroupId(), false)
                        external.social.updateInbox(notification)
                        writeUser(external, external.getEmail())
                        writeGroup(receiver, receiver.getGroupId())
                    }
                }
            }
        }

    } //UPDATE

    fun removeMember(data: MemberInput){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val external = readUser(data.externalMember)

        if(verifyLoggedUser(user) && verifyUser(external)){
            val toBeRemoved = Member(data.externalMember, 0)
            when(data.idCommunity.isNullOrBlank()){
                true->{
                    val groupId = getGroupId(data.groupName, data.creator)
                    val receiver = readGroup(groupId)
                    if(verifyUser(external) && verifyGroup(receiver) && receiver.verifyMember(loggedUser) && !receiver.verifyMember(data.externalMember))
                        receiver.updateMember(toBeRemoved, true)
                    external.social.updateMemberIn(receiver.getGroupId(), true)
                    writeUser(external, external.getEmail())
                    writeGroup(receiver, receiver.getGroupId())
                }
                false->{
                    val groupId = getCommunityGroupId(data.idCommunity,getGroupId(data.groupName, data.creator))
                    val receiver = readGroup(groupId)
                    val community = readCommunity(getCommunityId(data.idCommunity))
                    if(verifyCommunity(community) && (loggedUser == receiver.getCreator() || loggedUser in community.getModerators())){
                        receiver.updateMember(toBeRemoved, true)
                        external.social.updateMemberIn(receiver.getGroupId(), true)
                        writeUser(external, external.getEmail())
                        writeGroup(receiver, receiver.getGroupId())
                    }
                }
            }
        }
    } //UPDATE

    fun deleteGroup(data: GroupOperationsData){ //NEEDS WORK FOR COMMUNITY
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)


        if(verifyLoggedUser(user)) {
            when(data.idCommunity.isNullOrBlank()){
                true->{
                    val groupId = getGroupId(data.groupName, loggedUser)
                    val receiver = readGroup(groupId)
                    if(verifyGroup(receiver) && receiver.getCreator() == loggedUser){

                        val members = receiver.getMembers()
                        for(i in 0 until members.size){
                            val member = readUser(members[i].userEmail)
                            if(verifyUser(member)) {
                                member.social.updateMemberIn(receiver.getGroupId(),true)
                                writeUser(member, member.getEmail())
                            }
                        }

                        user.social.updateMyGroups(groupId,true)
                        DeleteFile.getDeleteFileOperator().deleteGroup(receiver)
                        writeUser(user, loggedUser)
                    }
                }
                false->{
                    val community = readCommunity(getCommunityId(data.idCommunity))
                    val groupId = getCommunityGroupId(data.idCommunity,data.groupName)
                    val receiver = readGroup(groupId)
                    if(verifyCommunity(community) && (loggedUser == receiver.getCreator() || loggedUser in community.getModerators())){
                       when(community.groups.checkGroupApproval(receiver.getGroupId())){
                           true->{

                               val members = receiver.getMembers()
                               for(i in 0 until members.size){
                                   val member = readUser(members[i].userEmail)
                                   if(verifyUser(member)) {
                                       member.social.updateMemberIn(receiver.getGroupId(),true)
                                       writeUser(member, member.getEmail())
                                   }
                               }

                               community.groups.removeApprovedGroup(receiver.getGroupId())
                               user.social.updateMyGroups(receiver.getGroupId(),true)
                               DeleteFile.getDeleteFileOperator().deleteGroup(receiver)
                               writeUser(user, loggedUser)
                               writeCommunity(community, community.getId())
                           }
                           false->{

                               val members = receiver.getMembers()
                               for(i in 0 until members.size){
                                   val member = readUser(members[i].userEmail)
                                   if(verifyUser(member)) {
                                       member.social.updateMemberIn(receiver.getGroupId(),true)
                                       writeUser(member, member.getEmail())
                                   }
                               }
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