package br.meetingplace.management

import br.meetingplace.entities.grupos.GroupConversation
import br.meetingplace.entities.grupos.Member
import br.meetingplace.entities.grupos.UserMember
import br.meetingplace.entities.usuario.Conversation
import br.meetingplace.entities.usuario.Follower
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.notificacao.Inbox

class EntitiesManagement: GeneralEntitiesManagement() {

    //USERS
    fun follow(Friend: Follower){
        if(Friend.current == getUserLogged()){


            val indexExternal = getIndexUser(Friend.external)
            val indexCurrent = getIndexUser(Friend.current)
            val notification = Inbox("${userList[indexCurrent].userName} is now following you.", "New follower.")

            userList[indexExternal].inbox.add(notification)

            if( indexCurrent != -1 && indexExternal != -1)
                userList[indexCurrent].followers.add(Friend.external)
        }
    }

    fun unfollow(Friend: Follower){

        if(Friend.current == getUserLogged()){
            val indexExternal = getIndexUser(Friend.external)
            val indexCurrent = getIndexUser(Friend.current)

            if( indexCurrent != -1 && indexExternal != -1)
                userList[indexCurrent].followers.remove(Friend.external)

        }
    }

    fun messengerUser(chat: Conversation){

        val indexSender = getIndexUser(chat.sender)
        val indexReceiver = getIndexUser(chat.receiver)

        if( indexSender != -1 && indexReceiver != -1 && chat.sender == getUserLogged()){
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

    fun joinGroup(member: UserMember){

        if(member.id == getUserLogged()){

            val indexGroup = getIndexGroup(member.group)
            val indexUser = getIndexUser(member.id)
            if(indexGroup != -1 && indexUser != -1 )
                groupList[indexGroup].management.addMember(member)
        }
    }

    fun leaveGroup(member: UserMember){

        if(member.id == getUserLogged()){
            val indexGroup = getIndexGroup(member.group)
            val indexUser = getIndexUser(member.id)

            if(indexGroup != -1 && indexUser != -1 && member.id == getUserLogged() && groupList[indexGroup].getCreator() != member.id)
                groupList[indexGroup].management.removeMember(member)
        }
    }
    //USERS

    //GROUPS
    fun messengerGroup(conversation: GroupConversation){

        val indexUser = getIndexUser(conversation.sender)
        val indexGroup = getIndexGroup(conversation.group)
        if(conversation.sender == getUserLogged() && indexGroup != -1 ){

            val indexMember = getIndexMember(conversation.sender, conversation.group)
            if(indexMember != -1){ // If indexMember == 1 then the user isnt part of the group
                val message = conversation.message + userList[indexUser].userName
                groupList[indexGroup].sendMsg(conversation, getUserLogged())
            }
        }
    }

    fun addMember(member: UserMember){

        val indexGroup = getIndexGroup(member.group)
        val indexUser = getIndexUser(member.id)


        if(indexGroup != -1 && indexUser != -1 && member.id != getUserLogged() ){
            // checks if the logged user is on the group
            val indexMember = getIndexMember(member.id, member.group)
            if(indexMember == -1){ // isnt part of the group then add
                val mem = Member(member.id, 0)
                groupList[indexGroup].members.add(mem)
            }
        }
    }

    fun removeMember(member: UserMember){

        val user = member.id
        val indexGroup = getIndexGroup(member.group)
        val indexUser = getIndexUser(user)
        val indexMember = getIndexMember(user, member.group)

        // checks if the logged user is on the group and is an admin or the creator
        if(indexGroup != -1 && indexUser != -1 && user == getUserLogged() && indexMember != -1 && groupList[indexGroup].members[indexMember].role == 1)
            groupList[indexGroup].members.remove(groupList[indexGroup].members[indexMember])
    }
    //GROUPS






    /* ON HOLD


    fun addAdmin(admin: Perfil): Boolean{
        return if(!existeNoGrupo(admin)){
            var novoAdmin = perfilToMembro(admin)
            novoAdmin.mudarPermissão(EPermissoes.ADMIN)

            membros.add(novoAdmin)
            membrosAdmin.add(novoAdmin)
            true
        }
        else {
            var index = obterIndex(admin)
            if (membros[index].obterPermissao() == EPermissoes.ADMIN) {
                false
            } else {
                membros[index].mudarPermissão(EPermissoes.ADMIN)
                membrosAdmin.add(membros[index])
                true
            }
        }
    }

    fun removerAdmin(admin: Perfil): Int {
        if(membrosAdmin.size > 0){
            if(!existeNoGrupo(admin)){
                return -1
            }
            else if(!existeNoGrupoAdmin(admin)){
                return 0
            }
            else {
                var indexGrupo = obterIndex(admin)
                var indexGrupoAdmin = obterIndexAdmin(admin)

                membros[indexGrupo].mudarPermissão(EPermissoes.COMUM)
                membrosAdmin.removeAt(indexGrupoAdmin)
                return 1
            }
        }
        else
            return -2 //
    }

     */
}