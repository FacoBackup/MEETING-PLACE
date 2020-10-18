package br.meetingplace.management.interfaces.groups

import br.meetingplace.data.group.GroupOperations
import br.meetingplace.interfaces.file.DeleteFile
import br.meetingplace.interfaces.file.ReadFile
import br.meetingplace.interfaces.file.WriteFile
import br.meetingplace.interfaces.utility.Generator
import br.meetingplace.interfaces.utility.Path
import br.meetingplace.interfaces.utility.Refresh
import br.meetingplace.interfaces.utility.Verifiers
import br.meetingplace.servicies.groups.Group

interface Group: Refresh, ReadFile, WriteFile, Path, Generator, Verifiers, DeleteFile{

    fun createGroup(group: Group){
        val management = readLoggedUser().email

        if(verifyPath("users",management) && group.getId() == "" && verifyUserSocialProfile(management) && management != ""){
            val user = readUser(management)
            group.startGroup(generateId(), management)
            user.social.updateMyGroups(group.getId(),false)
            writeGroup(group.getId(),group)
            writeUser(management,user)
        }
    }

    fun deleteGroup(operations: GroupOperations){
        val management = readLoggedUser().email

        if(verifyPath("users",management) && verifyPath("groups",operations.idGroup) && verifyUserSocialProfile(management) && management != "") {
            val user = readUser(management)
            val group = readGroup(operations.idGroup)

            if(management == group.getCreator()){

                val members = group.getMembers()
                for(i in 0 until members.size){
                    if(verifyPath("users", members[i].userId)) {
                        val member = readUser(members[i].userId)
                        member.social.updateMemberIn(group.getId(),true)
                        writeUser(member.getEmail(), member)
                    }
                }
                user.social.updateMyGroups(group.getId(),true)
                if(verifyPath("chats", group.getChatId()))
                    delete(getPath("chats", group.getChatId()))

                delete(getPath("groups", group.getId()))
                writeUser(management, user)
            }
        }
    }
}


//
//    fun messengerGroup(conversation: GroupChatContent){
//
//        val management = readLoggedUser().email
//
//        if(verifyPath("users",management) && verifyPath("groups",conversation.groupId) && verifyUserSocialProfile(management) && management != "") {
//            val group = readGroup(conversation.groupId)
//            val user = readUser(management)
//            if(group.getChatId() == ""){ // CHAT DOESNT EXIST
//                val newChat = Chat(group.getId(), listOf(group.getCreator()))
//                writeChat(group.getId()+ "-chat",newChat)
//                val message = Message(conversation.message, generateId(), management,true)
//                newChat.addMessage(message)
//
//                val notification = Inbox("${user.social.getUserName()} started the group chat.","Group Message.")
//                val members = group.getMembers()
//                for (i in 0 until members.size){
//                    if(verifyPath("users", members[i].userId)){
//                        val member = readUser(members[i].userId)
//                        member.social.updateInbox(notification)
//                    }
//                }
//
//            }
//            else{
//                val chat = readGroupChat(group.getId()+"-chat")
//
//            }
//        }
//
//    }
//
//    fun addMember(member: UserMember){
//         val management = readLoggedUser().email
//        val indexGroup = getGroupIndex(member.group)
//        val indexUser = getUserIndex(member.id)
//        val mem = Member(member.id, 0)
//        val logged = getLoggedUser()
//
//        if(indexGroup != -1 && indexUser != -1 && member.id != logged && logged != -1 && verifyUserSocialProfile(getLoggedUser())){
//            // checks if the logged user is on the group
//            val indexMember = getMemberIndex(member.id, member.group)
//            if(indexMember == -1) // isnt part of the group then add
//                groupList[indexGroup].members.add(mem)
//
//        }
//    }
//
//    fun removeMember(member: UserMember){
//         val management = readLoggedUser().email
//        val user = member.id
//        val indexGroup = getGroupIndex(member.group)
//        val indexUser = getUserIndex(user)
//        val indexMember = getMemberIndex(user, member.group)
//        val logged = getLoggedUser()
//        // checks if the logged user is on the group and is an admin or the creator
//        if(indexGroup != -1 && indexUser != -1 && indexMember != -1 && logged != -1 && verifyUserSocialProfile(getLoggedUser())){
//            if(user != getLoggedUser() && groupList[indexGroup].members[indexMember].role == 1)
//                groupList[indexGroup].members.remove(groupList[indexGroup].members[indexMember])
//
//            else
//                groupList[indexGroup].members.remove(groupList[indexGroup].members[indexMember])
//        }
//
//    }
//
