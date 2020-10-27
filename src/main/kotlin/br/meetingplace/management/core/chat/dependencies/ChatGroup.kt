package br.meetingplace.management.core.chat.dependencies

import br.meetingplace.data.chat.ChatComplexOperations
import br.meetingplace.data.chat.ChatMessage
import br.meetingplace.data.chat.ChatOperations
import br.meetingplace.management.core.operators.Generator
import br.meetingplace.management.core.operators.Verify
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteGroup
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.chat.Message
import br.meetingplace.services.notification.Inbox

class ChatGroup private constructor(): ChatInterface, ReadWriteUser, ReadWriteLoggedUser, ReadWriteGroup, Verify, Generator, IDs{

    companion object{
        private val op = ChatGroup()
        fun getChatGroupOperator()= op
    }

    override fun sendMessage(data: ChatMessage) {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val group = readGroup(getGroupId(data.idReceiver, data.creator))
        val msg = Message(data.message, generateId(), loggedUser, true)
        val notification = Inbox("${user.social.getUserName()} from ${group.getGroupId()} sent a new message.", "Group Message.")

        if (verifyLoggedUser(user) && verifyGroup(group)) {
            val groupMembers = group.getMembers()
            val updatedChat = group.getChat()
            val chat = group.getChat()
            chat.addMessage(msg)
            group.updateChat(updatedChat)
            writeGroup(group,group.getGroupId())

            for (i in 0 until groupMembers.size) {
                val member = readUser(groupMembers[i].userEmail)
                if (verifyUser(member) && groupMembers[i].userEmail != loggedUser) {
                    member.social.updateInbox(notification)
                    writeUser(member, member.getEmail())
                }//member exists
            }//for
            val creator = readUser(group.getCreator())
            if(loggedUser != group.getCreator() && verifyUser(creator)){

                creator.social.updateInbox(notification)
                writeUser(creator, creator.getEmail())
            }
        }
    }

    override fun favoriteMessage(data: ChatOperations){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val group = readGroup(getGroupId(data.idReceiver, data.creator))

        if(verifyLoggedUser(user) && verifyGroup(group)){
            val chat = group.getChat()
            chat.favoriteMessage(data)
            group.updateChat(chat)
            writeGroup(group, group.getGroupId())
        }
    }//UPDATE

    override fun unFavoriteMessage(data: ChatOperations){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val group = readGroup(getGroupId(data.idReceiver, data.creator))

        if(verifyLoggedUser(user) && verifyGroup(group)){
            val chat = group.getChat()
            chat.unFavoriteMessage(data)
            group.updateChat(chat)
            writeGroup(group, group.getGroupId())
        }
    }//UPDATE

    override fun quoteMessage(data: ChatComplexOperations){ // NEEDS WORK HERE
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val group = readGroup(getGroupId(data.idReceiver, data.creator))

        if(verifyLoggedUser(user) && verifyGroup(group)){
            val chat = group.getChat()
            if(chat.verifyMessage(data.idMessage)){
                chat.quoteMessage(data, generateId())
                group.updateChat(chat)
                writeGroup(group,group.getGroupId())
            }
        }
    }//UPDATE

    override fun shareMessage(data: ChatComplexOperations) {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val group = readGroup(getGroupId(data.idReceiver, data.creator))

        if(verifyLoggedUser(user) && verifyGroup(group)){
            val chat = group.getChat()
            val messageContent = chat.shareMessage(data)
            if(messageContent != ""){
                val sharedMessage = ChatMessage("|Shared| $messageContent", data.idReceiver, true, data.creator,data.idCommunity)
                sendMessage(sharedMessage)
            }
        }
    }

    override fun deleteMessage(data: ChatOperations){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val group = readGroup(getGroupId(data.idReceiver, data.creator))

        if(verifyLoggedUser(user) && verifyGroup(group)){
            val chat = group.getChat()
            if(chat.verifyMessage(data.idMessage)){
                chat.deleteMessage(data)
                group.updateChat(chat)
                writeGroup(group, group.getGroupId())
            }
        }
    }//DELETE
}