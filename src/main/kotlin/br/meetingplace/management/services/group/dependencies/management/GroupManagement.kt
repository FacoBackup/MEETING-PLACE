package br.meetingplace.management.services.group.dependencies.management

import br.meetingplace.data.group.MemberInput
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.services.community.Community
import br.meetingplace.services.group.Group
import br.meetingplace.services.group.Member
import br.meetingplace.services.notification.Inbox

class GroupManagement private constructor(): GroupManagementInterface{
    private val iDs = IDsController.getClass()
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()

    companion object{
        private val Class = GroupManagement()
        fun getClass () = Class
    }

    override fun addMember(data: MemberInput){
        val loggedUser = rw.readLoggedUser().email
        val user = rw.readUser(loggedUser)
        val newMember = rw.readUser(data.member)
        lateinit var groupId: String
        lateinit var group: Group
        lateinit var community: Community
        lateinit var notification: Inbox

        if(verify.verifyUser(user) && verify.verifyUser(newMember)){
            val toBeAdded = Member(data.member, 0)
            when(data.idCommunity.isNullOrBlank()){
                true->{

                    groupId = iDs.simpleToStandardIdGroup(data.ID, user)
                    group = rw.readGroup(groupId)
                    notification = Inbox("You're now a member of ${group.getNameGroup()}", "Group.")

                    if(verify.verifyUser(newMember) && verify.verifyGroup(group) && group.verifyMember(loggedUser) && !group.verifyMember(data.member))
                        group.updateMember(toBeAdded, false)
                    newMember.updateMemberIn(group.getGroupId(), false)
                    newMember.updateInbox(notification)
                    rw.writeUserToFile(newMember,iDs.attachNameToEmail(newMember.getUserName(),newMember.getEmail()))

                    rw.writeGroup(group, group.getGroupId())
                }
                false->{
                    groupId =iDs.simpleToStandardIdGroup(data.ID, user)
                    group = rw.readGroup(groupId)
                    community = rw.readCommunity(iDs.getCommunityId(data.idCommunity))
                    notification = Inbox("You're now a member of ${group.getNameGroup()}", "Group.")

                    if(verify.verifyCommunity(community) && (loggedUser == group.getCreator() || loggedUser in community.getModerators())){
                        group.updateMember(toBeAdded, false)
                        newMember.updateMemberIn(group.getGroupId(), false)
                        newMember.updateInbox(notification)
                        rw.writeUserToFile(newMember,iDs.attachNameToEmail(newMember.getUserName(),newMember.getEmail()))
                        rw.writeGroup(group, group.getGroupId())
                    }
                }
            }
        }

    } //UPDATE

    override fun removeMember(data: MemberInput){
        val loggedUser = rw.readLoggedUser().email
        val user = rw.readUser(loggedUser)
        val member = rw.readUser(data.member)
        lateinit var toBeRemoved: Member
        lateinit var groupId: String
        lateinit var group: Group
        lateinit var community: Community

        if(verify.verifyUser(user) && verify.verifyUser(member)){
            toBeRemoved = Member(data.member, 0)
            when(data.idCommunity.isNullOrBlank()){
                true->{
                    groupId =iDs.simpleToStandardIdGroup(data.ID, member)
                    group = rw.readGroup(groupId)
                    if(verify.verifyGroup(group) && group.verifyMember(loggedUser) && !group.verifyMember(data.member))
                        group.updateMember(toBeRemoved, true)
                    member.updateMemberIn(group.getGroupId(), true)
                    rw.writeUserToFile(member,iDs.attachNameToEmail(member.getUserName(),member.getEmail()))
                    rw.writeGroup(group, group.getGroupId())
                }
                false->{ //COMMUNITY
                    groupId =iDs.simpleToStandardIdGroup(data.ID, member)
                    group = rw.readGroup(groupId)
                    community = rw.readCommunity(iDs.getCommunityId(data.idCommunity))
                    if(verify.verifyCommunity(community) && (loggedUser == group.getCreator() || loggedUser in community.getModerators())){
                        group.updateMember(toBeRemoved, true)
                        member.updateMemberIn(group.getGroupId(), true)
                        rw.writeUserToFile(member,iDs.attachNameToEmail(member.getUserName(),member.getEmail()))
                        rw.writeGroup(group, group.getGroupId())
                    }
                }
            }
        }
    } //UPDATE
}