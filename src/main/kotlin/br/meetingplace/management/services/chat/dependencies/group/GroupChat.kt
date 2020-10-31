package br.meetingplace.management.services.chat.dependencies.group

import br.meetingplace.data.chat.ChatMessage
import br.meetingplace.data.chat.ChatOperations
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.management.services.chat.dependencies.BaseChatInterface
import br.meetingplace.services.chat.Chat
import br.meetingplace.services.chat.Message
import br.meetingplace.services.group.Member
import br.meetingplace.services.notification.Inbox

class GroupChat private constructor(): BaseChatInterface{
    private val iDs = IDsController.getClass()
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()

    companion object{
        private val Class = GroupChat()
        fun getClass()= Class
    }

    override fun sendMessage(data: ChatMessage) {
        val logged = rw.readLoggedUser().fileName
        val user = rw.readUser(if(!logged.isNullOrBlank()) logged else "")

        val group = rw.readGroup(iDs.simpleToStandardIdGroup(data.idReceiver, user))
        lateinit var msg: Message
        lateinit var notification: Inbox
        lateinit var  groupMembers:List<Member>
        lateinit var  updatedChat:Chat
        lateinit var  chat:Chat

        if (verify.verifyUser(user) && verify.verifyGroup(group)) {
            msg = Message(data.message, iDs.generateId(), logged!!, true)
            notification = Inbox("${user.getUserName()} from ${group.getGroupId()} sent a new message.", "Group Message.")

            groupMembers = group.getMembers()
            updatedChat = group.getChat()
            chat = group.getChat()
            chat.addMessage(msg)
            group.updateChat(updatedChat)
            rw.writeGroup(group,group.getGroupId())
            for (i in groupMembers.indices) {
                val member = rw.readUser(groupMembers[i].userEmail)
                if (verify.verifyUser(member) && groupMembers[i].userEmail != logged) {
                    member.updateInbox(notification)
                    rw.writeUserToFile(member,iDs.attachNameToEmail(member.getUserName(),member.getEmail()))
                }//member exists
            }//for

            val creator = rw.readUser(group.getCreator())
            if(logged != group.getCreator() && verify.verifyUser(creator)){
                creator.updateInbox(notification)
                rw.writeUserToFile(creator,iDs.attachNameToEmail(creator.getUserName(),creator.getEmail()))
            }
        }
    }

    override fun deleteMessage(data: ChatOperations) {
        val logged = rw.readLoggedUser().fileName
        val user = rw.readUser(if(!logged.isNullOrBlank()) logged else "")

        val group = rw.readGroup(iDs.simpleToStandardIdGroup(data.idReceiver, user))
        if(verify.verifyUser(user) && verify.verifyGroup(group)){
            val chat = group.getChat()
            if(chat.verifyMessage(data.idMessage)){
                chat.deleteMessage(data)
                group.updateChat(chat)
                rw.writeGroup(group, group.getGroupId())
            }
        }
    }
}