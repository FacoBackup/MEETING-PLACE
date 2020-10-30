package br.meetingplace.management.services.chat.dependencies.reader

import br.meetingplace.data.Data
import br.meetingplace.management.dependencies.IDs
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteChat
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteGroup
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.management.services.search.dependecies.group.GroupSearchInterface
import br.meetingplace.services.chat.Chat
import br.meetingplace.services.entitie.User
import br.meetingplace.services.group.Group

class ChatReader private constructor(): GroupSearchInterface, ChatReaderInterface, ReadWriteGroup, ReadWriteLoggedUser, ReadWriteUser, ReadWriteChat, IDs,Verify{

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
            group = readGroup(simpleToStandardIdGroup(data.ID, user))
            return when(verifyGroup(group)){
                true->{ // is a group
                    group.getChat()
                }
                false->{ //maybe a user
                    externalUser = readUser(data.ID)
                    if(verifyUser(externalUser)) {
                        readChat(getChatId(logged, externalUser.getEmail()))
                    } else null
                }
            }
        }
        return null
    }
}