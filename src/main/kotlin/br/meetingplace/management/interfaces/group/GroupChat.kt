package br.meetingplace.management.interfaces.group

import br.meetingplace.data.group.MemberInput
import br.meetingplace.data.chat.ChatComplexOperations
import br.meetingplace.data.chat.ChatMessage
import br.meetingplace.data.chat.ChatOperations
import br.meetingplace.management.interfaces.file.ReadFile
import br.meetingplace.management.interfaces.file.WriteFile
import br.meetingplace.management.interfaces.utility.Generator
import br.meetingplace.management.interfaces.utility.Refresh
import br.meetingplace.management.interfaces.utility.Verifiers
import br.meetingplace.management.interfaces.ConditionsVerifiers
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.chat.Message
import br.meetingplace.servicies.notification.Inbox

interface GroupChat: ReadFile, WriteFile, Refresh, Generator, Verifiers, ConditionsVerifiers {

    fun sendMessage(content: ChatMessage) {
        val management = readLoggedUser().email

        val chatId = content.id + "-chat"
        if (groupChatConditions(content.id, management)) {
            val user = readUser(management)
            val group = readGroup(content.id)
            val groupMembers = group.getMembers()
            val msg = Message(content.message, generateId(), management, true)
            val notification = Inbox("${user.social.getUserName()} from ${group.getNameGroup()} sent a new message.", "Group Message.")
            val chat = group.getChat()

            chat.addMessage(msg)
            group.updateChat(chat)
            writeGroup(group.getId(),group)

            for (i in 0 until groupMembers.size) {
                if (verifyPath("users", groupMembers[i].userEmail) && groupMembers[i].userEmail != management) {
                    val member = readUser(groupMembers[i].userEmail)
                    member.social.updateInbox(notification)
                    writeUser(member.getEmail(), member)
                }//member exists
            }//for

            if(management != group.getCreator() && verifyPath("users", group.getCreator())){
                val creator = readUser(group.getCreator())
                creator.social.updateInbox(notification)
                writeUser(creator.getEmail(), creator)
            }
        }
    }

    fun favoriteMessage(message: ChatOperations){

        val management = readLoggedUser().email

        if(groupChatConditions(message.id, management)){
            val group = readGroup(message.id)
            val chat = group.getChat()
            val operation= ChatOperations(message.idMessage, "")

            chat.favoriteMessage(operation)
            group.updateChat(chat)
            writeGroup(group.getId(),group)
        }
    }//UPDATE

    fun unFavoriteMessage(message: ChatOperations){

        val management = readLoggedUser().email

        if(groupChatConditions(message.id, management)){
            val group = readGroup(message.id)
            val chat = group.getChat()
            val operation= ChatOperations(message.idMessage, "")
            chat.unFavoriteMessage(operation)
            group.updateChat(chat)
            writeGroup(group.getId(),group)
        }
    }//UPDATE

    fun quoteMessage(message: ChatComplexOperations){ // NEEDS WORK HERE

        val management = readLoggedUser().email

        if(groupChatConditions(message.id, management)){
            val group = readGroup(message.id)
            val chat = group.getChat()
            if(chat.verifyMessage(message.idMessage)){
                chat.quoteMessage(message, generateId())
                group.updateChat(chat)
                writeGroup(group.getId(),group)
            }
        }
    }//UPDATE

    fun deleteMessage(message: ChatOperations){
        val management = readLoggedUser().email

        if(groupChatConditions(message.id, management)){
            val group = readGroup(message.id)
            val chat = group.getChat()
            val operation= ChatOperations(message.idMessage, "")
            chat.deleteMessage(operation)
            group.updateChat(chat)
            writeGroup(group.getId(),group)
        }
    }//DELETE
}