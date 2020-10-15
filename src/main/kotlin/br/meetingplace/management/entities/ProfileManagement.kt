package br.meetingplace.management.entities

import br.meetingplace.data.conversation.operations.ChatOperations
import br.meetingplace.data.conversation.ChatContent
import br.meetingplace.data.entities.user.Follower
import br.meetingplace.data.entities.group.UserMember
import br.meetingplace.data.startup.SocialProfileData
import br.meetingplace.entities.user.profiles.SocialProfile
import br.meetingplace.servicies.notification.Inbox

open class ProfileManagement: GroupManagement() {

    fun createSocialProfile(newProfile: SocialProfileData){

        if(getLoggedUser() != -1 && newProfile.ProfileName !in nameList){
            val social = SocialProfile(newProfile.ProfileName, newProfile.gender, newProfile.nationality, newProfile.about)
            val indexUser = getUserIndex(getLoggedUser())

            userList[indexUser].createSocialProfile(social)
            nameList.add(userList[indexUser].social.getUserName())
        }
    }

/*
    fun createProfessionalProfile(user: ProfessionalProfile){ // NEEDS WORK

        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser()))
            createProfessionalProfile(user)
    }

 */

    fun follow(data: Follower){

        if(getLoggedUser() != -1){
            val indexExternal = getUserIndex(data.external)
            val indexCurrent = getUserIndex(getLoggedUser())
            val notification = Inbox("${userList[indexCurrent].social.getUserName()} is now following you.", "New follower.")

            if(indexExternal != -1 && verifyFollower(data) == 0){ // verifies if the user you want to follow exists
                userList[indexExternal].social.updateInbox(notification)
                userList[indexExternal].social.followers.add(getLoggedUser())
                userList[indexCurrent].social.following.add(data.external)
            }
        }
    }

    fun unfollow(data: Follower){

        val logged = getLoggedUser()
        if(logged != -1 && verifyUserSocialProfile(logged)){
            val indexExternal = getUserIndex(data.external)
            val indexCurrent = getUserIndex(logged)

            if( indexCurrent != -1 && indexExternal != -1){
                userList[indexCurrent].social.following.remove(data.external)
                userList[indexExternal].social.followers.remove(logged)
            }
        }
    }
/*
    fun messengerUser(chat: ChatContent){

        val indexReceiver = getUserIndex(chat.receiver)
        val logged = getLoggedUser()
        if(logged != -1 && indexReceiver != -1 && chat.receiver != logged && verifyUserSocialProfile(logged)){
            val indexSender = getUserIndex(logged)
            val chatId = userList[indexReceiver].getId() + userList[indexSender].getId()

            if(userList[indexReceiver].social.getChatIndex(chatId) != -1){ // The conversation already exists
                val notification = Inbox("${userList[indexSender].social.userName} sent a new message.", "Message.")
                chat.message+=" - "+userList[indexSender].social.userName
                userList[indexReceiver].social.newMessage(chat, chatId)
                userList[indexReceiver].social.updateInbox(notification)
            }
            else { // The conversation doesn't exist
                val notification = Inbox("${userList[indexSender].social.userName} started a conversation with you.", "Message.")
                val newChat = Chat(chatId)
                newChat.conversation.add(chat.message+" - "+userList[indexSender].social.userName)

                userList[indexReceiver].social.startChat(newChat)
                userList[indexReceiver].social.updateInbox(notification)
                userList[indexSender].social.startChat(newChat)
            }
        }
    }

 */
    fun sendMessage(message: ChatContent){
        val indexReceiver = getUserIndex(message.receiver)
        val indexUser = getUserIndex(getLoggedUser())
        val chatId = getLoggedUser() + message.receiver

        if(getLoggedUser() != -1 && indexReceiver != -1){

        }
    }


    fun deleteMessage(message: ChatOperations){

        val indexReceiver = getUserIndex(message.receiver)
        val indexUser = getUserIndex(getLoggedUser())
        val chatId = getLoggedUser() + message.receiver

        if(getLoggedUser() != -1 && indexReceiver != -1){

        }
    }

    fun leaveGroup(member: UserMember){

        val logged = getLoggedUser()
        if( logged != -1){

            val indexGroup = getGroupIndex(member.group)

            if(indexGroup != -1 && groupList[indexGroup].getCreator() != logged)
                removeMember(member)
        }
    }


}