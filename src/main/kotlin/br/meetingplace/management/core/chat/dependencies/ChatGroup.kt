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

class ChatGroup private constructor(): ChatInterface, ReadWriteUser, ReadWriteLoggedUser, ReadWriteGroup, Verify, Generator{

    companion object{
        private val op = ChatGroup()
        fun getChatGroupOperator()= op
    }

    override fun sendMessage(data: ChatMessage) {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val receiver = readGroup(data.idReceiver)
        val msg = Message(data.message, generateId(), loggedUser, true)
        val notification = Inbox("${user.social.getUserName()} from ${receiver.getGroupId()} sent a new message.", "Group Message.")

        if (verifyUser(user) && verifyGroup(receiver)) {
            val groupMembers = receiver.getMembers()
            val updatedChat = receiver.getChat()
            val chat = receiver.getChat()
            chat.addMessage(msg)
            receiver.updateChat(updatedChat)
            writeGroup(receiver,receiver.getGroupId())

            for (i in 0 until groupMembers.size) {
                val member = readUser(groupMembers[i].userEmail)
                if (verifyUser(member) && groupMembers[i].userEmail != loggedUser) {
                    member.social.updateInbox(notification)
                    writeUser(member, member.getEmail())
                }//member exists
            }//for
            val creator = readUser(receiver.getCreator())
            if(loggedUser != receiver.getCreator() && verifyUser(creator)){

                creator.social.updateInbox(notification)
                writeUser(creator, creator.getEmail())
            }
        }
    }

    override fun favoriteMessage(data: ChatOperations){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val receiver = readGroup(data.idReceiver)

        if(verifyUser(user) && verifyGroup(receiver)){
            val chat = receiver.getChat()
            chat.favoriteMessage(data)
            receiver.updateChat(chat)
            writeGroup(receiver, receiver.getGroupId())
        }
    }//UPDATE

    override fun unFavoriteMessage(data: ChatOperations){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val receiver = readGroup(data.idReceiver)

        if(verifyUser(user) && verifyGroup(receiver)){
            val chat = receiver.getChat()
            chat.unFavoriteMessage(data)
            receiver.updateChat(chat)
            writeGroup(receiver, receiver.getGroupId())
        }
    }//UPDATE

    override fun quoteMessage(data: ChatComplexOperations){ // NEEDS WORK HERE
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val receiver = readGroup(data.idReceiver)

        if(verifyUser(user) && verifyGroup(receiver)){
            val chat = receiver.getChat()
            if(chat.verifyMessage(data.idMessage)){
                chat.quoteMessage(data, generateId())
                receiver.updateChat(chat)
                writeGroup(receiver,receiver.getGroupId())
            }
        }
    }//UPDATE

    override fun shareMessage(data: ChatComplexOperations) {
        TODO("Not yet implemented")
    }

    override fun deleteMessage(data: ChatOperations){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val receiver = readGroup(data.idReceiver)

        if(verifyUser(user) && verifyGroup(receiver)){
            val chat = receiver.getChat()
            if(chat.verifyMessage(data.idMessage)){
                chat.deleteMessage(data)
                receiver.updateChat(chat)
                writeGroup(receiver, receiver.getGroupId())
            }
        }
    }//DELETE
}