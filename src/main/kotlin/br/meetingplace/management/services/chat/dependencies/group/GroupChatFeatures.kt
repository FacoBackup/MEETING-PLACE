package br.meetingplace.management.services.chat.dependencies.group

import br.meetingplace.data.chat.ChatComplexOperations
import br.meetingplace.data.chat.ChatMessage
import br.meetingplace.data.chat.ChatOperations
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.management.services.chat.controller.ChatController
import br.meetingplace.management.services.chat.dependencies.ChatFeaturesInterface

class GroupChatFeatures private constructor(): ChatFeaturesInterface{
    private val iDs = IDsController.getClass()
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()

    companion object{
        private val Class = GroupChatFeatures()
        fun getClass()= Class
    }

    override fun favoriteMessage(data: ChatOperations){
        val logged = rw.readLoggedUser().fileName
        val user = rw.readUser(if(!logged.isNullOrBlank()) logged else "")

        val group = rw.readGroup(iDs.simpleToStandardIdGroup(data.idReceiver, user))

        if(verify.verifyUser(user) && verify.verifyGroup(group)){
            val chat = group.getChat()
            chat.favoriteMessage(data)
            group.updateChat(chat)
            rw.writeGroup(group, group.getGroupId())
        }
    }//UPDATE

    override fun unFavoriteMessage(data: ChatOperations){
        val logged = rw.readLoggedUser().fileName
        val user = rw.readUser(if(!logged.isNullOrBlank()) logged else "")

        val group = rw.readGroup(iDs.simpleToStandardIdGroup(data.idReceiver, user))

        if(verify.verifyUser(user) && verify.verifyGroup(group)){
            val chat = group.getChat()
            chat.unFavoriteMessage(data)
            group.updateChat(chat)
            rw.writeGroup(group, group.getGroupId())
        }
    }//UPDATE

    override fun quoteMessage(data: ChatComplexOperations){ // NEEDS WORK HERE
        val logged = rw.readLoggedUser().fileName
        val user = rw.readUser(if(!logged.isNullOrBlank()) logged else "")

        val group = rw.readGroup(iDs.simpleToStandardIdGroup(data.idReceiver, user))

        if(verify.verifyUser(user) && verify.verifyGroup(group)){
            val chat = group.getChat()
            if(chat.verifyMessage(data.idMessage)){
                chat.quoteMessage(data, iDs.generateId())
                group.updateChat(chat)
                rw.writeGroup(group,group.getGroupId())
            }
        }
    }//UPDATE

    override fun shareMessage(data: ChatComplexOperations) {
        val logged = rw.readLoggedUser().fileName
        val user = rw.readUser(if(!logged.isNullOrBlank()) logged else "")

        val group = rw.readGroup(iDs.simpleToStandardIdGroup(data.idReceiver, user))

        if(verify.verifyUser(user) && verify.verifyGroup(group)){
            val chat = group.getChat()
            val messageContent = chat.shareMessage(data)
            if(messageContent != ""){
                val sharedMessage = ChatMessage("|Shared| $messageContent", data.idReceiver, true,data.idCommunity)
                ChatController.getClass().sendMessage(sharedMessage)
            }
        }
    }

}