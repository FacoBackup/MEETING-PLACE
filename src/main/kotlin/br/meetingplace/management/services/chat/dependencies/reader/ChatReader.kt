package br.meetingplace.management.services.chat.dependencies.reader

import br.meetingplace.data.Data
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.verify.dependencies.Verify
import br.meetingplace.management.dependencies.readwrite.dependencies.chat.ReadWriteChat
import br.meetingplace.management.dependencies.readwrite.dependencies.group.ReadWriteGroup
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteUser
import br.meetingplace.services.chat.Chat
import br.meetingplace.services.entitie.User
import br.meetingplace.services.group.Group

class ChatReader private constructor(): ChatReaderInterface, ReadWriteGroup, ReadWriteLoggedUser, ReadWriteUser, ReadWriteChat, Verify {
    private val iDs = IDsController.getClass()
    companion object{
        private val Class = ChatReader()
        fun getClass () = Class
    }

    override fun seeChat(data: Data): Chat?{
        val logged = readLoggedUser().email
        val user = readUser(logged)
        lateinit var group: Group
        lateinit var externalUser: User
        if(verifyLoggedUser(user)){
            group = readGroup(iDs.simpleToStandardIdGroup(data.ID, user))
            return when(verifyGroup(group)){
                true->{ // is a group
                    group.getChat()
                }
                false->{ //maybe a user
                    externalUser = readUser(data.ID)
                    if(verifyUser(externalUser)) {
                        readChat(iDs.getChatId(logged, externalUser.getEmail()))
                    } else null
                }
            }
        }
        return null
    }
}