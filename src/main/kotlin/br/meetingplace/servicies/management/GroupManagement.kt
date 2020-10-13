package br.meetingplace.servicies.management

import br.meetingplace.data.conversation.GroupConversation
import br.meetingplace.data.user.Member
import br.meetingplace.data.user.UserMember
import br.meetingplace.entities.groups.Group

open class GroupManagement:ThreadManagement() {

    fun createGroup(group: Group){

        group.updateCreator(getLoggedUser())
        val indexCreator = getUserIndex(getLoggedUser())

        if(getLoggedUser() != -1 && group.getId() == -1 && verifyGroupName(group.getNameGroup()) && verifyUserSocialProfile(getLoggedUser())){
            group.startGroup(generateGroupId())
            userList[indexCreator].social.groups.add(group.getId())
            groupList.add(group)
        }
    }

    fun deleteGroup(member: UserMember){

        val indexGroup = getGroupIndex(member.group)

        if(getLoggedUser() != -1 && indexGroup != -1 && groupList[indexGroup].getCreator() == member.id && member.id == getLoggedUser() && verifyUserSocialProfile(getLoggedUser())){ //ONLY THE CREATOR OF THE GROUP CAN DELETE IT
            // percorre a lista de usuarios e remove todos que fazem parte do grupo
            for(i in 0 until userList.size)
                userList[i].social.groups.remove(member.group) // removing the group id from the users profile

            groupList.remove(groupList[indexGroup])
        }
    }

    fun messengerGroup(conversation: GroupConversation){

        val logged = getLoggedUser()
        val indexGroup = getGroupIndex(conversation.group)
        if(logged != -1 && indexGroup != -1 && verifyUserSocialProfile(getLoggedUser())){
            val indexUser = getUserIndex(logged)
            groupList[indexGroup].sendMsg(conversation.message +" - " + userList[indexUser].social.userName, logged)
        }

    }

    fun addMember(member: UserMember){

        val indexGroup = getGroupIndex(member.group)
        val indexUser = getUserIndex(member.id)
        val mem = Member(member.id, 0)
        val logged = getLoggedUser()

        if(indexGroup != -1 && indexUser != -1 && member.id != logged && logged != -1 && verifyUserSocialProfile(getLoggedUser())){
            // checks if the logged user is on the group
            val indexMember = getMemberIndex(member.id, member.group)
            if(indexMember == -1) // isnt part of the group then add
                groupList[indexGroup].members.add(mem)

        }
    }

    fun removeMember(member: UserMember){

        val user = member.id
        val indexGroup = getGroupIndex(member.group)
        val indexUser = getUserIndex(user)
        val indexMember = getMemberIndex(user, member.group)
        val logged = getLoggedUser()
        // checks if the logged user is on the group and is an admin or the creator
        if(indexGroup != -1 && indexUser != -1 && indexMember != -1 && logged != -1 && verifyUserSocialProfile(getLoggedUser())){
            if(user != getLoggedUser() && groupList[indexGroup].members[indexMember].role == 1)
                groupList[indexGroup].members.remove(groupList[indexGroup].members[indexMember])

            else
                groupList[indexGroup].members.remove(groupList[indexGroup].members[indexMember])
        }

    }

}