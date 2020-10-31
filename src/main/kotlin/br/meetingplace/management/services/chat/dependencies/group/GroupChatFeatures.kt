package br.meetingplace.management.services.chat.dependencies.group

import br.meetingplace.data.chat.ChatComplexOperations
import br.meetingplace.data.chat.ChatMessage
import br.meetingplace.data.chat.ChatOperations
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.verify.dependencies.Verify
import br.meetingplace.management.dependencies.readwrite.dependencies.group.ReadWriteGroup
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteUser
import br.meetingplace.management.services.chat.controller.ChatController
import br.meetingplace.management.services.chat.dependencies.ChatFeaturesInterface

class GroupChatFeatures private constructor(): ChatFeaturesInterface, ReadWriteLoggedUser, ReadWriteUser, ReadWriteGroup, Verify {
    private val iDs = IDsController.getClass()

    companion object{
        private val Class = GroupChatFeatures()
        fun getClass()= Class
    }

    override fun favoriteMessage(data: ChatOperations){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val group = readGroup(iDs.simpleToStandardIdGroup(data.idReceiver, user))

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
        val group = readGroup(iDs.simpleToStandardIdGroup(data.idReceiver, user))

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
        val group = readGroup(iDs.simpleToStandardIdGroup(data.idReceiver, user))

        if(verifyLoggedUser(user) && verifyGroup(group)){
            val chat = group.getChat()
            if(chat.verifyMessage(data.idMessage)){
                chat.quoteMessage(data, iDs.generateId())
                group.updateChat(chat)
                writeGroup(group,group.getGroupId())
            }
        }
    }//UPDATE

    override fun shareMessage(data: ChatComplexOperations) {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val group = readGroup(iDs.simpleToStandardIdGroup(data.idReceiver, user))

        if(verifyLoggedUser(user) && verifyGroup(group)){
            val chat = group.getChat()
            val messageContent = chat.shareMessage(data)
            if(messageContent != ""){
                val sharedMessage = ChatMessage("|Shared| $messageContent", data.idReceiver, true,data.idCommunity)
                ChatController.getClass().sendMessage(sharedMessage)
            }
        }
    }

}