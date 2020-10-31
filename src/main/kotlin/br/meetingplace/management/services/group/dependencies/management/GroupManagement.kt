package br.meetingplace.management.services.group.dependencies.management

import br.meetingplace.data.group.MemberInput
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.verify.dependencies.Verify
import br.meetingplace.management.dependencies.readwrite.dependencies.community.ReadWriteCommunity
import br.meetingplace.management.dependencies.readwrite.dependencies.group.ReadWriteGroup
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteUser
import br.meetingplace.services.community.Community
import br.meetingplace.services.group.Group
import br.meetingplace.services.group.Member
import br.meetingplace.services.notification.Inbox

class GroupManagement private constructor(): GroupManagementInterface, ReadWriteLoggedUser, ReadWriteUser, ReadWriteGroup, ReadWriteCommunity, Verify {
    private val iDs = IDsController.getClass()
    companion object{
        private val Class = GroupManagement()
        fun getClass () = Class
    }

    override fun addMember(data: MemberInput){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val newMember = readUser(data.member)
        lateinit var groupId: String
        lateinit var group: Group
        lateinit var community: Community
        lateinit var notification: Inbox

        if(verifyLoggedUser(user) && verifyUser(newMember)){
            val toBeAdded = Member(data.member, 0)
            when(data.idCommunity.isNullOrBlank()){
                true->{

                    groupId = iDs.simpleToStandardIdGroup(data.ID, user)
                    group = readGroup(groupId)
                    notification = Inbox("You're now a member of ${group.getNameGroup()}", "Group.")

                    if(verifyUser(newMember) && verifyGroup(group) && group.verifyMember(loggedUser) && !group.verifyMember(data.member))
                        group.updateMember(toBeAdded, false)
                    newMember.updateMemberIn(group.getGroupId(), false)
                    newMember.updateInbox(notification)
                    writeUserToFile(newMember,iDs.attachNameToEmail(newMember.getUserName(),newMember.getEmail()))

                    writeGroup(group, group.getGroupId())
                }
                false->{
                    groupId =iDs.simpleToStandardIdGroup(data.ID, user)
                    group = readGroup(groupId)
                    community = readCommunity(iDs.getCommunityId(data.idCommunity))
                    notification = Inbox("You're now a member of ${group.getNameGroup()}", "Group.")

                    if(verifyCommunity(community) && (loggedUser == group.getCreator() || loggedUser in community.getModerators())){
                        group.updateMember(toBeAdded, false)
                        newMember.updateMemberIn(group.getGroupId(), false)
                        newMember.updateInbox(notification)
                        writeUserToFile(newMember,iDs.attachNameToEmail(newMember.getUserName(),newMember.getEmail()))
                        writeGroup(group, group.getGroupId())
                    }
                }
            }
        }

    } //UPDATE

    override fun removeMember(data: MemberInput){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val member = readUser(data.member)
        lateinit var toBeRemoved: Member
        lateinit var groupId: String
        lateinit var group: Group
        lateinit var community: Community

        if(verifyLoggedUser(user) && verifyUser(member)){
            toBeRemoved = Member(data.member, 0)
            when(data.idCommunity.isNullOrBlank()){
                true->{
                    groupId =iDs.simpleToStandardIdGroup(data.ID, member)
                    group = readGroup(groupId)
                    if(verifyUser(member) && verifyGroup(group) && group.verifyMember(loggedUser) && !group.verifyMember(data.member))
                        group.updateMember(toBeRemoved, true)
                    member.updateMemberIn(group.getGroupId(), true)
                    writeUserToFile(member,iDs.attachNameToEmail(member.getUserName(),member.getEmail()))
                    writeGroup(group, group.getGroupId())
                }
                false->{ //COMMUNITY
                    groupId =iDs.simpleToStandardIdGroup(data.ID, member)
                    group = readGroup(groupId)
                    community = readCommunity(iDs.getCommunityId(data.idCommunity))
                    if(verifyCommunity(community) && (loggedUser == group.getCreator() || loggedUser in community.getModerators())){
                        group.updateMember(toBeRemoved, true)
                        member.updateMemberIn(group.getGroupId(), true)
                        writeUserToFile(member,iDs.attachNameToEmail(member.getUserName(),member.getEmail()))
                        writeGroup(group, group.getGroupId())
                    }
                }
            }
        }
    } //UPDATE
}