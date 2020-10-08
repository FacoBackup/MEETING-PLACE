package br.meetingplace.application

import br.meetingplace.entities.grupos.UserMember
import br.meetingplace.entities.usuario.Conversation
import br.meetingplace.entities.usuario.Follower
import br.meetingplace.entities.usuario.Profile
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.notificacao.Inbox


open class UserManagement:GeneralManagement() {

    override fun addUser(user: Profile){ //NÃO DEVEMOS USAR O NOME DE USUARIO PARA PEGAR INFORMAÇÃO NENHUMA, TUDO DEVE DEPENDER DO ID RANDOMICO
        println("estive aqui")
        if(user.getId() == -1 && verifyUserName(user.userName)){
            user.updateId(generateIdUser())
            userList.add(user)
        }
    }

    override fun removeUser(User: Int){

        if(User == logged){

            val indexUser = getIndexUser(User)
            var member:UserMember

            if(indexUser != -1){

                for(i in 0 until userList.size)   // percorre a lista de usuarios e remove todos os que tem o usuario como amigo
                    userList[i].followers.remove(User)

                for(i in 0 until groupList.size){
                    member = UserMember(User,groupList[i].getId())
                    groupList[i].management.removeMember(member)
                }

                userList.remove(userList[indexUser])
            }
        }
    }

    //FRIEND
    override fun follow(Friend: Follower){

        if(Friend.current == logged){


            val indexExternal = getIndexUser(Friend.external)
            val indexCurrent = getIndexUser(Friend.current)
            val notification = Inbox("${userList[indexCurrent].userName} is now following you.", "New follower.")

            userList[indexExternal].inbox.add(notification)

            if( indexCurrent != -1 && indexExternal != -1)
                userList[indexCurrent].followers.add(Friend.external)
        }
    }

    override fun unfollow(Friend: Follower){

        if(Friend.current == logged){
            val indexExternal = getIndexUser(Friend.external)
            val indexCurrent = getIndexUser(Friend.current)

            if( indexCurrent != -1 && indexExternal != -1)
                userList[indexCurrent].followers.remove(Friend.external)

        }
    }

    override fun messenger(chat: Conversation){

        val indexSender = getIndexUser(chat.sender)
        val indexReceiver = getIndexUser(chat.receiver)

        if( indexSender != -1 && indexReceiver != -1 && chat.sender == logged){
            val chatId = userList[indexReceiver].getId() + userList[indexSender].getId()
            val indexChatSender = getChatIndex(indexSender, chatId)
            val indexChatReceiver = getChatIndex(indexReceiver, chatId)


            if(indexChatReceiver != -1){
                val notification = Inbox("${userList[indexSender].userName} sent a new message.", "Message.")
                userList[indexReceiver].chat[indexChatReceiver].conversation.add(chat.message+" - "+userList[indexSender].userName)
                userList[indexReceiver].inbox.add(notification)
                userList[indexSender].chat[indexChatReceiver].conversation.add(chat.message+" - "+userList[indexSender].userName)

            }
            else{
                val notification = Inbox("${userList[indexSender].userName} started a conversation with you.", "Message.")
                val newChat = Chat()
                newChat.conversation.add(chat.message+" - "+userList[indexSender].userName)
                newChat.id = chatId

                userList[indexReceiver].chat.add(newChat)
                userList[indexReceiver].inbox.add(notification)
                userList[indexSender].chat.add(newChat)
            }
        }
    }

    override fun joinGroup(member: UserMember){

        if(member.id == logged){

            val indexGroup = getIndexGroup(member.group)
            val indexUser = getIndexUser(member.id)
            if(indexGroup != -1 && indexUser != -1 )
                groupList[indexGroup].management.addMember(member)
        }
    }

    override fun leaveGroup(member: UserMember){

        if(member.id == logged){
            val indexGroup = getIndexGroup(member.group)
            val indexUser = getIndexUser(member.id)

            if(indexGroup != -1 && indexUser != -1 && member.id == logged && groupList[indexGroup].getCreator() != member.id)
                groupList[indexGroup].management.removeMember(member)
        }
    }

    private fun getChatIndex(indexUser: Int, chatId: Int): Int{
        for(i in 0 until userList[indexUser].chat.size){
            if(userList[indexUser].chat[i].id == chatId)
                return i
        }
        return -1
    }
}