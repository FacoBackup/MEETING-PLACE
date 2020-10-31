package br.meetingplace.management.services.chat.dependencies.group

import br.meetingplace.data.chat.ChatMessage
import br.meetingplace.data.chat.ChatOperations
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.verify.dependencies.Verify
import br.meetingplace.management.dependencies.readwrite.dependencies.group.ReadWriteGroup
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteUser
import br.meetingplace.management.services.chat.dependencies.BaseChatInterface
import br.meetingplace.services.chat.Chat
import br.meetingplace.services.chat.Message
import br.meetingplace.services.group.Member
import br.meetingplace.services.notification.Inbox

class GroupChat private constructor(): BaseChatInterface, ReadWriteUser, ReadWriteLoggedUser, ReadWriteGroup, Verify {
    private val iDs = IDsController.getClass()

    companion object{
        private val Class = GroupChat()
        fun getClass()= Class
    }

    override fun sendMessage(data: ChatMessage) {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val group = readGroup(iDs.simpleToStandardIdGroup(data.idReceiver, user))
        lateinit var msg: Message
        lateinit var notification: Inbox
        lateinit var  groupMembers:List<Member>
        lateinit var  updatedChat:Chat
        lateinit var  chat:Chat

        if (verifyLoggedUser(user) && verifyGroup(group)) {
            msg = Message(data.message, iDs.generateId(), loggedUser, true)
            notification = Inbox("${user.getUserName()} from ${group.getGroupId()} sent a new message.", "Group Message.")

            groupMembers = group.getMembers()
            updatedChat = group.getChat()
            chat = group.getChat()
            chat.addMessage(msg)
            group.updateChat(updatedChat)
            writeGroup(group,group.getGroupId())
            for (i in groupMembers.indices) {
                val member = readUser(groupMembers[i].userEmail)
                if (verifyUser(member) && groupMembers[i].userEmail != loggedUser) {
                    member.updateInbox(notification)
                    writeUserToFile(member,iDs.attachNameToEmail(member.getUserName(),member.getEmail()))
                }//member exists
            }//for

            val creator = readUser(group.getCreator())
            if(loggedUser != group.getCreator() && verifyUser(creator)){
                creator.updateInbox(notification)
                writeUserToFile(creator,iDs.attachNameToEmail(creator.getUserName(),creator.getEmail()))
            }
        }
    }

    override fun deleteMessage(data: ChatOperations) {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val group = readGroup(iDs.simpleToStandardIdGroup(data.idReceiver, user))
        if(verifyLoggedUser(user) && verifyGroup(group)){
            val chat = group.getChat()
            if(chat.verifyMessage(data.idMessage)){
                chat.deleteMessage(data)
                group.updateChat(chat)
                writeGroup(group, group.getGroupId())
            }
        }
    }
}