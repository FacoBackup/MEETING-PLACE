package br.meetingplace.management.services.group.dependencies.factory

import br.meetingplace.data.group.GroupData
import br.meetingplace.data.group.GroupOperationsData
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.verify.dependencies.Verify
import br.meetingplace.management.dependencies.ReadWriteController
import br.meetingplace.management.dependencies.readwrite.dependencies.community.ReadWriteCommunity
import br.meetingplace.management.dependencies.readwrite.dependencies.group.ReadWriteGroup
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteUser
import br.meetingplace.services.community.Community
import br.meetingplace.services.group.Group
import br.meetingplace.services.group.Member
import br.meetingplace.services.notification.Inbox

class GroupFactory private constructor(): GroupFactoryInterface, Verify, ReadWriteUser, ReadWriteGroup, ReadWriteCommunity {
    private val iDs = IDsController.getClass()
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
            id = iDs.getGroupId(data.groupName, loggedUser)
            when(data.idCommunity.isNullOrBlank()){
                true->{
                    newGroup.startGroup(data.groupName, id,data.about, loggedUser)
                    user.updateMyGroups(newGroup.getGroupId(),false)
                    writeGroup(newGroup, newGroup.getGroupId())
                    writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
                }
                false->{
                    val community = readCommunity(iDs.getCommunityId(data.idCommunity))
                    if(verifyCommunity(community)){
                        communityMods = community.getModerators()
                        notification = Inbox("$loggedUser wants to create a new group in ${community.getName()}.", "Community Group")

                        for(element in communityMods){
                            val mod = readUser(element)
                            if (verifyUser(mod) && mod != user)
                                mod.updateInbox(notification)
                        }
                        //the verify community method already insures that the id and name are different of null so don't mind the !!
                        id = iDs.getCommunityGroupId(community.getId()!!, data.groupName)

                        newGroup.startGroup(data.groupName, id,data.about, loggedUser)
                        user.updateMyGroups(newGroup.getGroupId(),false)

                        if(loggedUser !in communityMods)
                            community.updateGroupsInValidation(newGroup.getGroupId(), null)
                        else
                            community.updateGroupsInValidation(newGroup.getGroupId(), true)

                        writeGroup(newGroup, newGroup.getGroupId())
                        writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
                        writeCommunity(community, community.getId()!!)
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
                    groupId = iDs.getGroupId(data.groupName, loggedUser)
                    receiver = readGroup(groupId)

                    if (verifyGroup(receiver) && receiver.getCreator() == loggedUser) {
                        members = receiver.getMembers()
                        for (element in members) {
                            val member = readUser(element.userEmail)
                            if (verifyUser(member)) {
                                member.updateMemberIn(receiver.getGroupId(), true)
                                writeUserToFile(member,iDs.attachNameToEmail(member.getUserName(),member.getEmail()))
                            }
                        }

                        user.updateMyGroups(groupId, true)
                        ReadWriteController.getDeleteFileOperator().deleteGroup(receiver)
                        writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
                    }
                }
                false -> {
                    community = readCommunity(iDs.getCommunityId(data.idCommunity))
                    groupId = iDs.getCommunityGroupId(data.idCommunity, data.groupName)
                    receiver = readGroup(groupId)

                    if (verifyCommunity(community) && (loggedUser == receiver.getCreator() || loggedUser in community.getModerators())) {
                        when (community.checkGroupApproval(receiver.getGroupId())) {
                            true -> {
                                members = receiver.getMembers()
                                for (element in members) {
                                    val member = readUser(element.userEmail)
                                    if (verifyUser(member)) {
                                        member.updateMemberIn(receiver.getGroupId(), true)
                                        writeUserToFile(member,iDs.attachNameToEmail(member.getUserName(),member.getEmail()))
                                    }
                                }

                                community.removeApprovedGroup(receiver.getGroupId())
                                user.updateMyGroups(receiver.getGroupId(), true)
                                ReadWriteController.getDeleteFileOperator().deleteGroup(receiver)
                                writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
                                //the verify community method already insures that the id and name are different of null so don't mind the !!
                                writeCommunity(community, community.getId()!!)
                            }
                            false -> {

                                members = receiver.getMembers()
                                for (element in members) {
                                    val member = readUser(element.userEmail)
                                    if (verifyUser(member)) {
                                        member.updateMemberIn(receiver.getGroupId(), true)
                                        writeUserToFile(member,iDs.attachNameToEmail(member.getUserName(),member.getEmail()))
                                    }
                                }

                                community.updateGroupsInValidation(receiver.getGroupId(), false)
                                user.updateMyGroups(receiver.getGroupId(), true)
                                ReadWriteController.getDeleteFileOperator().deleteGroup(receiver)
                                writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
                                //the verify community method already insures that the id and name are different of null so don't mind the !!
                                writeCommunity(community, community.getId()!!)
                            }//false
                        }//when
                    }//if
                }//false
            }//when
        }//if
    }//method
}