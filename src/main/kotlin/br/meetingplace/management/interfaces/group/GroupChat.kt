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

        when (groupChatConditions(content.id, management)) {
            1 -> { // The Chat already exists
                val user = readUser(management)
                val group = readGroup(content.id)
                val groupMembers = group.getMembers()
                val msg = Message(content.message, generateId(), management, true)
                val notification = Inbox("${user.social.getUserName()} sent a new message.", "Group Message.")
                val chat = readChat(chatId)
                chat.addMessage(msg)

                writeChat(chatId, chat)

                for (i in 0 until groupMembers.size) {
                    if (verifyPath("users", groupMembers[i].userEmail)) {
                        val member = readUser(groupMembers[i].userEmail)
                        member.social.updateInbox(notification)
                        writeUser(member.getEmail(), member)
                    }//member exists
                }//for
                println("done")
            }

            2 -> { // The Chat doesn't exists
                val user = readUser(management)
                val group = readGroup(content.id)
                val groupMembers = group.getMembers()
                val notification = Inbox("${user.social.getUserName()} started the group conversation.", "Group Message.")
                val newChat = Chat(chatId, listOf(group.getCreator()))
                val msg = Message(content.message, generateId(), management, true)
                newChat.addMessage(msg)
                group.updateChat(chatId)
                writeChat(chatId, newChat)
                writeGroup(group.getId(), group)
                //SENDING THE NOTIFICATION TO ALL MEMBERS
                for (i in 0 until groupMembers.size) {
                    if (verifyPath("users", groupMembers[i].userEmail)) {
                        val member = readUser(groupMembers[i].userEmail)
                        member.social.updateInbox(notification)
                        writeUser(member.getEmail(), member)
                    }
                }
            }
        }
    }

    fun getGroupChat(member: MemberInput):Chat {

        val management = readLoggedUser().email
        val nullChat = Chat("", listOf())

        return if(groupConditions(member.groupId, management)){
            val group = readGroup(member.groupId)
            if(group.verifyMember(management) && group.getChatId() != "" && verifyPath("chats", group.getChatId()))
                readChat(group.getChatId())
            else nullChat
        }
        else nullChat
    }//READ

    fun favoriteMessage(message: ChatOperations){

        val management = readLoggedUser().email

        if(groupConditions(message.id, management)){
            val group = readGroup(message.id)
            val chat = readChat(group.getChatId())
            val operation= ChatOperations(message.idMessage, "")
            chat.favoriteMessage(operation)
            writeChat(group.getChatId(),chat)
        }
    }//UPDATE

    fun unFavoriteMessage(message: ChatOperations){

        val management = readLoggedUser().email

        if(groupConditions(message.id, management)){
            val group = readGroup(message.id)
            val chat = readChat(group.getChatId())
            val operation= ChatOperations(message.idMessage, "")
            chat.unFavoriteMessage(operation)
            writeChat(group.getChatId(),chat)
        }
    }//UPDATE

    fun quoteMessage(message: ChatComplexOperations){ // NEEDS WORK HERE

        val management = readLoggedUser().email

        if(groupConditions(message.id, management)){
            val group = readGroup(message.id)
            val chat = readChat(group.getChatId())
            if(chat.verifyMessage(message.idMessage)){
                //val notification = Inbox("${user.social.getUserName()} sent a new message.", "Message.")
                chat.quoteMessage(message, generateId())
                writeChat(group.getChatId(),chat)
            }
        }
    }//UPDATE

    fun deleteMessage(message: ChatOperations){
        val management = readLoggedUser().email

        if(groupConditions(message.id, management)){
            val group = readGroup(message.id)
            val chat = readChat(group.getChatId())
            val operation= ChatOperations(message.idMessage, "")
            chat.deleteMessage(operation)
            writeChat(group.getChatId(),chat)
        }
    }//DELETE
}