package br.meetingplace.servicies.management

import br.meetingplace.data.GroupConversation
import br.meetingplace.data.Member
import br.meetingplace.data.UserMember
import br.meetingplace.data.Conversation
import br.meetingplace.data.Follower
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.notification.Inbox

class EntitiesManagement: GeneralEntitiesManagement() {

    //USERS
    fun follow(data: Follower){

        if(getUserLogged() != -1){
            val indexExternal = getIndexUser(data.external)
            val indexCurrent = getIndexUser(getUserLogged())
            val notification = Inbox("${userList[indexCurrent].userName} is now following you.", "New follower.")

            if(indexExternal != -1 && verifyFollower(data) == 0){ // verifies if the user you want to follow exists
                userList[indexExternal].inbox.add(notification)
                userList[indexExternal].followers.add(getUserLogged())
                userList[indexCurrent].following.add(data.external)
            }

        }
    }

    fun unfollow(data: Follower){

        if(getUserLogged() != -1){
            val indexExternal = getIndexUser(data.external)
            val indexCurrent = getIndexUser(getUserLogged())

            if( indexCurrent != -1 && indexExternal != -1){
                userList[indexCurrent].following.remove(data.external)
                userList[indexExternal].followers.remove(getUserLogged())
            }
        }
    }

    fun messengerUser(chat: Conversation){


        val indexReceiver = getIndexUser(chat.receiver)

        if(getUserLogged() != -1 && indexReceiver != -1 && chat.receiver != getUserLogged()){
            val indexSender = getIndexUser(getUserLogged())
            val chatId = userList[indexReceiver].getId() + userList[indexSender].getId()
            val indexChatReceiver = getChatIndex(indexReceiver, chatId)
            val indexChatSender = getChatIndex(indexSender, chatId)

            if(indexChatReceiver != -1){
                val notification = Inbox("${userList[indexSender].userName} sent a new message.", "Message.")
                userList[indexReceiver].chat[indexChatReceiver].conversation.add((chat.message+" - "+userList[indexSender].userName))
                userList[indexReceiver].inbox.add(notification)
                userList[indexSender].chat[indexChatSender].conversation.add((chat.message+" - "+userList[indexSender].userName))
            }
            else {
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

        if( getUserLogged() != -1){

            val indexGroup = getIndexGroup(member.group)
            if(indexGroup != -1)
                addMember(member)
        }
    }

    fun leaveGroup(member: UserMember){

        if( getUserLogged() != -1){

            val indexGroup = getIndexGroup(member.group)

            if(indexGroup != -1 && groupList[indexGroup].getCreator() != getUserLogged())
                removeMember(member)
        }
    }
    //USERS

    //GROUPS
    fun messengerGroup(conversation: GroupConversation){

        val indexUser = getIndexUser(getUserLogged())
        val indexGroup = getIndexGroup(conversation.group)
        if(getUserLogged() == getUserLogged() && indexGroup != -1 ){

            val indexMember = getIndexMember(getUserLogged(), conversation.group)
            if(indexMember != -1){ // If indexMember == 1 then the user isnt part of the group
                conversation.message += userList[indexUser].userName
                groupList[indexGroup].sendMsg(conversation, getUserLogged())
            }
        }
    }

    fun addMember(member: UserMember){

        val indexGroup = getIndexGroup(member.group)
        val indexUser = getIndexUser(member.id)
        val mem = Member(member.id, 0)

        if(indexGroup != -1 && indexUser != -1 && member.id != getUserLogged() ){
            // checks if the logged user is on the group
            val indexMember = getIndexMember(member.id, member.group)
            if(indexMember == -1){ // isnt part of the group then add

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