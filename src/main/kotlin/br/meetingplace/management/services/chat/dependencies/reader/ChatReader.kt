package br.meetingplace.management.services.chat.dependencies.reader

import br.meetingplace.data.Data
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.services.chat.Chat
import br.meetingplace.services.entitie.User
import br.meetingplace.services.group.Group

class ChatReader private constructor(): ChatReaderInterface{
    private val iDs = IDsController.getClass()
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()

    companion object{
        private val Class = ChatReader()
        fun getClass () = Class
    }

    override fun seeChat(data: Data): Chat?{
        val logged = rw.readLoggedUser().fileName
        val user = rw.readUser(if(!logged.isNullOrBlank()) logged else "")

        lateinit var group: Group
        lateinit var externalUser: User
        if(verify.verifyUser(user)){
            group = rw.readGroup(iDs.simpleToStandardIdGroup(data.ID, user))
            return when(verify.verifyGroup(group)){
                true->{ // is a group
                    group.getChat()
                }
                false->{ //maybe a user
                    externalUser = rw.readUser(iDs.attachNameToEmail(if(!data.name.isNullOrBlank()) data.name else "", data.ID))
                    if(verify.verifyUser(externalUser)) {
                        rw.readChat(iDs.getChatId(logged!!, externalUser.getEmail()))
                    } else null
                }
            }
        }
        return null
    }
}