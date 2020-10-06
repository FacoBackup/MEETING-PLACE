package br.meetingplace.application

import br.meetingplace.entities.grupos.UserMember
import br.meetingplace.entities.usuario.Conversation
import br.meetingplace.entities.usuario.Profile
import br.meetingplace.entities.usuario.UserFriend

open class UserManagement:GeneralManagement() {

    fun addUser(user: Profile){ //NÃO DEVEMOS USAR O NOME DE USUARIO PARA PEGAR INFORMAÇÃO NENHUMA, TUDO DEVE DEPENDER DO ID RANDOMICO

        if(user.getId() == -1){

            user.updateId(generateIdUser())
            userList.add(user)
        }
    }

    fun removeUser(User: Int){

        if(User == logged){

            val indexUser = getIndexUser(User)
            var member:UserMember

            if(indexUser != -1){

                for(i in 0 until userList.size)   // percorre a lista de usuarios e remove todos os que tem o usuario como amigo
                    userList[i].friends.remove(User)

                for(i in 0 until groupList.size){
                    member = UserMember(User,groupList[i].getId())
                    groupList[i].management.removeMember(member)
                }

                userList.remove(userList[indexUser])
            }
        }
    }

    //FRIEND
    fun addFriend(Friend: UserFriend){

        if(Friend.current == logged){
            val indexExternal = getIndexUser(Friend.external)
            val indexCurrent = getIndexUser(Friend.current)

            if( indexCurrent != -1 && indexExternal != -1)
                userList[indexCurrent].friends.add(Friend.external)
        }
    }

    fun removeFriend(Friend: UserFriend){

        if(Friend.current == logged){
            val indexExternal = getIndexUser(Friend.external)
            val indexCurrent = getIndexUser(Friend.current)

            if( indexCurrent != -1 && indexExternal != -1)
                userList[indexCurrent].friends.remove(Friend.external)

        }
    }

    fun messenger(chat: Conversation){

        if(chat.sender == logged){
            val indexSender = getIndexUser(chat.sender)
            val indexReceiver = getIndexUser(chat.receiver)

            if( indexSender != -1 && indexReceiver != -1){
                for(i in 0 until userList[indexSender].chat.size){
                    if(userList[indexSender].chat[i].id == userList[indexReceiver].getId() + userList[indexSender].getId())
                        userList[indexSender].chat[i].conversation.add(chat.message)
                }
            }
        }
    }

    fun joinGroup(member: UserMember){

        if(member.id == logged){

            val indexGroup = getIndexGroup(member.group)
            val indexUser = getIndexUser(member.id)
            if(indexGroup != -1 && indexUser != -1 )
                groupList[indexGroup].management.addMember(member)
        }
    }

    fun leaveGroup(member: UserMember){

        if(member.id == logged){
            val indexGroup = getIndexGroup(member.group)
            val indexUser = getIndexUser(member.id)

            if(indexGroup != -1 && indexUser != -1 && member.id == logged && groupList[indexGroup].getCreator() != member.id)
                groupList[indexGroup].management.removeMember(member)
        }
    }

}