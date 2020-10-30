package br.meetingplace.management.services.group.dependencies.factory

import br.meetingplace.data.group.GroupData
import br.meetingplace.data.group.GroupOperationsData
import br.meetingplace.management.dependencies.IDs
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.DeleteFile
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteCommunity
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteGroup
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.community.Community
import br.meetingplace.services.group.Group
import br.meetingplace.services.group.Member
import br.meetingplace.services.notification.Inbox

class GroupFactory private constructor(): GroupFactoryInterface, Verify, ReadWriteUser, IDs, ReadWriteGroup, ReadWriteCommunity{
    companion object{
        private val Class = GroupFactory()
        fun getClass () = Class
    }

    override fun create(data: GroupData) {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        lateinit var communityMods: List<String>
        lateinit var notification: Inbox
        lateinit var newGroup: Group
        lateinit var id: String

        if(verifyLoggedUser(user) && data.groupName.isNotEmpty()){

            newGroup = Group()
            id = getGroupId(data.groupName, loggedUser)
            when(data.idCommunity.isNullOrBlank()){
                true->{
                    newGroup.startGroup(data.groupName, id,data.about, loggedUser)
                    user.updateMyGroups(newGroup.getGroupId(),false)
                    writeGroup(newGroup, newGroup.getGroupId())
                    writeUser(user, loggedUser)
                }
                false->{
                    val community = readCommunity(getCommunityId(data.idCommunity))
                    if(verifyCommunity(community)){
                        communityMods = community.getModerators()
                        notification = Inbox("$loggedUser wants to create a new group in ${community.getName()}.", "Community Group")

                        for(element in communityMods){
                            val mod = readUser(element)
                            if (verifyUser(mod) && mod != user)
                                mod.updateInbox(notification)
                        }

                        id = getCommunityGroupId(community.getId(), data.groupName)

                        newGroup.startGroup(data.groupName, id,data.about, loggedUser)
                        user.updateMyGroups(newGroup.getGroupId(),false)

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
    }

    override fun delete(data: GroupOperationsData) {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        lateinit var groupId: String
        lateinit var receiver: Group
        lateinit var community: Community
        lateinit var members: List<Member>
        if(verifyLoggedUser(user)) {
            when (data.idCommunity.isNullOrBlank()) {
                true -> {
                    groupId = getGroupId(data.groupName, loggedUser)
                    receiver = readGroup(groupId)

                    if (verifyGroup(receiver) && receiver.getCreator() == loggedUser) {
                        members = receiver.getMembers()
                        for (element in members) {
                            val member = readUser(element.userEmail)
                            if (verifyUser(member)) {
                                member.updateMemberIn(receiver.getGroupId(), true)
                                writeUser(member, member.getEmail())
                            }
                        }

                        user.updateMyGroups(groupId, true)
                        DeleteFile.getDeleteFileOperator().deleteGroup(receiver)
                        writeUser(user, loggedUser)
                    }
                }
                false -> {
                    community = readCommunity(getCommunityId(data.idCommunity))
                    groupId = getCommunityGroupId(data.idCommunity, data.groupName)
                    receiver = readGroup(groupId)

                    if (verifyCommunity(community) && (loggedUser == receiver.getCreator() || loggedUser in community.getModerators())) {
                        when (community.groups.checkGroupApproval(receiver.getGroupId())) {
                            true -> {
                                members = receiver.getMembers()
                                for (element in members) {
                                    val member = readUser(element.userEmail)
                                    if (verifyUser(member)) {
                                        member.updateMemberIn(receiver.getGroupId(), true)
                                        writeUser(member, member.getEmail())
                                    }
                                }

                                community.groups.removeApprovedGroup(receiver.getGroupId())
                                user.updateMyGroups(receiver.getGroupId(), true)
                                DeleteFile.getDeleteFileOperator().deleteGroup(receiver)
                                writeUser(user, loggedUser)
                                writeCommunity(community, community.getId())
                            }
                            false -> {

                                members = receiver.getMembers()
                                for (element in members) {
                                    val member = readUser(element.userEmail)
                                    if (verifyUser(member)) {
                                        member.updateMemberIn(receiver.getGroupId(), true)
                                        writeUser(member, member.getEmail())
                                    }
                                }

                                community.groups.updateGroupsInValidation(receiver.getGroupId(), false)
                                user.updateMyGroups(receiver.getGroupId(), true)
                                DeleteFile.getDeleteFileOperator().deleteGroup(receiver)
                                writeUser(user, loggedUser)
                                writeCommunity(community, community.getId())
                            }//false
                        }//when
                    }//if
                }//false
            }//when
        }//if
    }//method
}