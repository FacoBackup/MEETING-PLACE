package br.meetingplace.management.interfaces.group

import br.meetingplace.data.group.GroupData
import br.meetingplace.data.group.GroupOperations
import br.meetingplace.data.group.MemberInput
import br.meetingplace.management.interfaces.file.DeleteFile
import br.meetingplace.management.interfaces.file.ReadFile
import br.meetingplace.management.interfaces.file.WriteFile
import br.meetingplace.management.interfaces.utility.Generator
import br.meetingplace.management.interfaces.utility.Path
import br.meetingplace.management.interfaces.utility.Refresh
import br.meetingplace.management.interfaces.utility.Verifiers
import br.meetingplace.servicies.group.Group
import br.meetingplace.servicies.group.Member
import br.meetingplace.servicies.notification.Inbox

interface Group: Refresh, ReadFile, WriteFile, Path, Generator, Verifiers, DeleteFile{

    fun createGroup(group: GroupData){
        val management = readLoggedUser().email

        if(verifyPath("users",management) && verifyUserSocialProfile() && management != ""){
            val user = readUser(management)
            val newGroup = Group()
            newGroup.startGroup(generateId(),group.name, group.about, management)
            user.social.updateMyGroups(newGroup.getId(),false)
            writeGroup(newGroup.getId(),newGroup)
            writeUser(management,user)
        }
    } // CREATE
    fun readMyGroups(): MutableList<Group> {

        val management = readLoggedUser().email
        val myGroups = mutableListOf<Group>()
        if(verifyPath("users",management) && verifyUserSocialProfile() && management != ""){
            val user = readUser(management)
            val myGroupsIds = user.social.getMyGroups()
            for (i in 0 until myGroupsIds.size){
                if(verifyPath("groups", myGroupsIds[i]))
                    myGroups.add(readGroup(myGroupsIds[i]))
            }
            return myGroups
        }
        return myGroups
    } // READ
    fun readMemberIn(): MutableList<Group> {

        val management = readLoggedUser().email
        val memberIn = mutableListOf<Group>()
        if(verifyPath("users",management) && verifyUserSocialProfile() && management != ""){
            val user = readUser(management)
            val groupsIds = user.social.getMemberIn()
            for (i in 0 until groupsIds.size){
                if(verifyPath("groups", groupsIds[i]))
                    memberIn.add(readGroup(groupsIds[i]))
            }
            return memberIn
        }
        return memberIn
    } // READ
    fun addMember(member: MemberInput){
        val management = readLoggedUser().email

        if(verifyPath("users", management) && verifyPath("groups", member.groupId) && verifyUserSocialProfile() && verifyPath("users", member.externalMember)){
            val external = readUser(member.externalMember)
            val group = readGroup(member.groupId)

            if(group.verifyMember(management) && !group.verifyMember(member.externalMember)){
                val toBeAdded = Member(member.externalMember,group.getMemberRole(management))
                val notification = Inbox("You're now a member of ${group.getNameGroup()}", "Group.")

                group.updateMember(toBeAdded, false)
                external.social.updateMemberIn(group.getId(), false)

                external.social.updateInbox(notification)

                writeUser(external.getEmail(), external)
                writeGroup(group.getId(), group)
            }
        }

    } //UPDATE
    fun removeMember(member: MemberInput){
        val management = readLoggedUser().email

        if(verifyPath("users", management) && verifyPath("groups", member.groupId) && verifyUserSocialProfile() && verifyPath("users", member.externalMember)){
            val external = readUser(member.externalMember)
            val group = readGroup(member.groupId)

            if(group.verifyMember(management) && group.verifyMember(member.externalMember) && group.getMemberRole(management) == 1){
                val toBeRemoved = Member(member.externalMember,group.getMemberRole(management))
                group.updateMember(toBeRemoved, true)
                external.social.updateMemberIn(group.getId(), true)
                writeUser(external.getEmail(), external)
                writeGroup(group.getId(), group)
            }
        }
    } //UPDATE
    fun deleteGroup(operations: GroupOperations){
        val management = readLoggedUser().email

        if(verifyPath("users",management) && verifyPath("groups",operations.idGroup) && verifyUserSocialProfile() && management != "") {
            val user = readUser(management)
            val group = readGroup(operations.idGroup)

            if(management == group.getCreator()){

                val members = group.getMembers()
                for(i in 0 until members.size){
                    if(verifyPath("users", members[i].userEmail)) {
                        val member = readUser(members[i].userEmail)
                        member.social.updateMemberIn(group.getId(),true)
                        writeUser(member.getEmail(), member)
                    }
                }
                user.social.updateMyGroups(group.getId(),true)
                if(verifyPath("chats", group.getChatId()))
                    delete(getPath("chats", group.getChatId()))

                delete(getPath("groups", group.getId()))
                writeUser(management, user)
            }//DELETE CHAT FROM GROUP
        }
    } //DELETE
}