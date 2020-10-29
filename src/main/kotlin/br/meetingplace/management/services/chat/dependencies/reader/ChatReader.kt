package br.meetingplace.management.services.chat.dependencies.reader

import br.meetingplace.data.Data

class ChatReader: ChatReaderInterface{
    override fun seeChat(data: Data) {
        TODO("Not yet implemented")
    }

    override fun seeGroupChat(data: Data) {
        TODO("Not yet implemented")
    }
    /*
    fun getMyChats(): List<Chat> {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val chats = mutableListOf<Chat>()

        if(verifyUser(user)){
            val userChats = user.social.getMyChats()
            for(i in 0 until userChats.size){
                val chat = readChat(userChats[i])
                if (verifyChat(chat))
                    chats.add(chat)
            }
            return chats
        }
        return chats
    }

     */
}