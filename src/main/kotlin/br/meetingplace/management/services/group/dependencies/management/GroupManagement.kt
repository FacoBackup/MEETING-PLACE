package br.meetingplace.management.services.group.dependencies.management

import br.meetingplace.data.group.MemberInput
import br.meetingplace.management.dependencies.IDs
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteCommunity
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteGroup
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.community.Community
import br.meetingplace.services.group.Group
import br.meetingplace.services.group.Member
import br.meetingplace.services.notification.Inbox

class GroupManagement private constructor(): GroupManagementInterface, ReadWriteLoggedUser, ReadWriteUser, ReadWriteGroup, ReadWriteCommunity,Verify, IDs{

    companion object{
        private val Class = GroupManagement()
        fun getClass () = Class
    }

    override fun addMember(data: MemberInput){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val external = readUser(data.member)
        lateinit var groupId: String
        lateinit var receiver: Group
        lateinit var community: Community
        lateinit var notification: Inbox

        if(verifyLoggedUser(user) && verifyUser(external)){
            val toBeAdded = Member(data.member, 0)
            when(data.idCommunity.isNullOrBlank()){
                true->{
                    groupId = getGroupId(data.ID, data.creator)
                    receiver = readGroup(groupId)
                    notification = Inbox("You're now a member of ${receiver.getNameGroup()}", "Group.")

                    if(verifyUser(external) && verifyGroup(receiver) && receiver.verifyMember(loggedUser) && !receiver.verifyMember(data.member))
                        receiver.updateMember(toBeAdded, false)
                    external.updateMemberIn(receiver.getGroupId(), false)
                    external.updateInbox(notification)
                    writeUser(external, external.getEmail())
                    writeGroup(receiver, receiver.getGroupId())
                }
                false->{
                    groupId = getCommunityGroupId(data.idCommunity,getGroupId(data.ID, data.creator))
                    receiver = readGroup(groupId)
                    community = readCommunity(getCommunityId(data.idCommunity))
                    notification = Inbox("You're now a member of ${receiver.getNameGroup()}", "Group.")

                    if(verifyCommunity(community) && (loggedUser == receiver.getCreator() || loggedUser in community.getModerators())){

                        receiver.updateMember(toBeAdded, false)
                        external.updateMemberIn(receiver.getGroupId(), false)
                        external.updateInbox(notification)
                        writeUser(external, external.getEmail())
                        writeGroup(receiver, receiver.getGroupId())
                    }
                }
            }
        }

    } //UPDATE

    override fun removeMember(data: MemberInput){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val external = readUser(data.member)
        lateinit var toBeRemoved: Member
        lateinit var groupId: String
        lateinit var receiver: Group
        lateinit var community: Community

        if(verifyLoggedUser(user) && verifyUser(external)){
            toBeRemoved = Member(data.member, 0)
            when(data.idCommunity.isNullOrBlank()){
                true->{
                    groupId = getGroupId(data.ID, data.creator)
                    receiver = readGroup(groupId)
                    if(verifyUser(external) && verifyGroup(receiver) && receiver.verifyMember(loggedUser) && !receiver.verifyMember(data.member))
                        receiver.updateMember(toBeRemoved, true)
                    external.updateMemberIn(receiver.getGroupId(), true)
                    writeUser(external, external.getEmail())
                    writeGroup(receiver, receiver.getGroupId())
                }
                false->{
                    groupId = getCommunityGroupId(data.idCommunity,getGroupId(data.ID, data.creator))
                    receiver = readGroup(groupId)
                    community = readCommunity(getCommunityId(data.idCommunity))
                    if(verifyCommunity(community) && (loggedUser == receiver.getCreator() || loggedUser in community.getModerators())){
                        receiver.updateMember(toBeRemoved, true)
                        external.updateMemberIn(receiver.getGroupId(), true)
                        writeUser(external, external.getEmail())
                        writeGroup(receiver, receiver.getGroupId())
                    }
                }
            }
        }
    } //UPDATE
}