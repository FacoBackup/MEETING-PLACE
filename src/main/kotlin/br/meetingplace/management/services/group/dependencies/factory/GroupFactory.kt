package br.meetingplace.management.services.group.dependencies.factory

import br.meetingplace.data.group.GroupData
import br.meetingplace.data.group.GroupOperationsData
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.services.community.Community
import br.meetingplace.services.group.Group
import br.meetingplace.services.group.Member
import br.meetingplace.services.notification.Inbox

class GroupFactory private constructor(): GroupFactoryInterface{

    private val iDs = IDsController.getClass()
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()

    companion object{
        private val Class = GroupFactory()
        fun getClass () = Class
    }

    override fun create(data: GroupData) {
        val loggedUser = rw.readLoggedUser().email
        val user = rw.readUser(loggedUser)

        lateinit var communityMods: List<String>
        lateinit var notification: Inbox
        lateinit var newGroup: Group
        lateinit var id: String

        if(verify.verifyUser(user) && data.groupName.isNotEmpty()){

            newGroup = Group()
            id = iDs.getGroupId(data.groupName, loggedUser)
            when(data.idCommunity.isNullOrBlank()){
                true->{
                    newGroup.startGroup(data.groupName, id,data.about, loggedUser)
                    user.updateMyGroups(newGroup.getGroupId(),false)
                    rw.writeGroup(newGroup, newGroup.getGroupId())
                    rw.writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
                }
                false->{
                    val community = rw.readCommunity(iDs.getCommunityId(data.idCommunity))
                    if(verify.verifyCommunity(community)){
                        communityMods = community.getModerators()
                        notification = Inbox("$loggedUser wants to create a new group in ${community.getName()}.", "Community Group")

                        for(element in communityMods){
                            val mod = rw.readUser(element)
                            if (verify.verifyUser(mod) && mod != user)
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

                        rw.writeGroup(newGroup, newGroup.getGroupId())
                        rw.writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
                        rw.writeCommunity(community, community.getId()!!)
                    }
                }
            }
        }
    }

    override fun delete(data: GroupOperationsData) {
        val loggedUser = rw.readLoggedUser().email
        val user = rw.readUser(loggedUser)

        lateinit var groupId: String
        lateinit var receiver: Group
        lateinit var community: Community
        lateinit var members: List<Member>

        if(verify.verifyUser(user)) {
            when (data.idCommunity.isNullOrBlank()) {
                true -> {
                    groupId = iDs.getGroupId(data.groupName, loggedUser)
                    receiver = rw.readGroup(groupId)

                    if (verify.verifyGroup(receiver) && receiver.getCreator() == loggedUser) {
                        members = receiver.getMembers()
                        for (element in members) {
                            val member = rw.readUser(element.userEmail)
                            if (verify.verifyUser(member)) {
                                member.updateMemberIn(receiver.getGroupId(), true)
                                rw.writeUserToFile(member,iDs.attachNameToEmail(member.getUserName(),member.getEmail()))
                            }
                        }

                        user.updateMyGroups(groupId, true)
                        rw.deleteGroup(receiver)
                        rw.writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
                    }
                }
                false -> {
                    community = rw.readCommunity(iDs.getCommunityId(data.idCommunity))
                    groupId = iDs.getCommunityGroupId(data.idCommunity, data.groupName)
                    receiver = rw.readGroup(groupId)

                    if (verify.verifyCommunity(community) && (loggedUser == receiver.getCreator() || loggedUser in community.getModerators())) {
                        when (community.checkGroupApproval(receiver.getGroupId())) {
                            true -> {
                                members = receiver.getMembers()
                                for (element in members) {
                                    val member = rw.readUser(element.userEmail)
                                    if (verify.verifyUser(member)) {
                                        member.updateMemberIn(receiver.getGroupId(), true)
                                        rw.writeUserToFile(member,iDs.attachNameToEmail(member.getUserName(),member.getEmail()))
                                    }
                                }

                                community.removeApprovedGroup(receiver.getGroupId())
                                user.updateMyGroups(receiver.getGroupId(), true)
                                rw.deleteGroup(receiver)
                                rw.writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
                                //the verify community method already insures that the id and name are different of null so don't mind the !!
                                rw. writeCommunity(community, community.getId()!!)
                            }
                            false -> {

                                members = receiver.getMembers()
                                for (element in members) {
                                    val member = rw.readUser(element.userEmail)
                                    if (verify.verifyUser(member)) {
                                        member.updateMemberIn(receiver.getGroupId(), true)
                                        rw.writeUserToFile(member,iDs.attachNameToEmail(member.getUserName(),member.getEmail()))
                                    }
                                }

                                community.updateGroupsInValidation(receiver.getGroupId(), false)
                                user.updateMyGroups(receiver.getGroupId(), true)
                                rw.deleteGroup(receiver)
                                rw.writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
                                //the verify community method already insures that the id and name are different of null so don't mind the !!
                                rw.writeCommunity(community, community.getId()!!)
                            }//false
                        }//when
                    }//if
                }//false
            }//when
        }//if
    }//method
}