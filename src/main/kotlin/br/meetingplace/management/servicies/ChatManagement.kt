package br.meetingplace.management.servicies

import br.meetingplace.data.conversation.operations.ChatOperations
import br.meetingplace.management.GeneralManagement
import br.meetingplace.servicies.chat.Message

open class ChatManagement: GeneralManagement() {

    fun sendMessage(message: Message){
        if(getLoggedUser() != -1){

        }
    }

    fun deleteMessage(message: ChatOperations){
        if(getLoggedUser() != -1){

        }
    }

    fun favoriteMessage(message: ChatOperations){
        if(getLoggedUser() != -1){

        }
    }

    fun unFavoriteMessage(message: ChatOperations){
        if(getLoggedUser() != -1){

        }
    }

    fun quoteMessage(message: Message){
        if(getLoggedUser() != -1){

        }
    }

    fun shareMessage(operations: ChatOperations){
        if(getLoggedUser() != -1 && ){
            val message =
        }
    }
}