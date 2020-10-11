package br.meetingplace.servicies.management

import br.meetingplace.data.*
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.conversationThread.MainThread
import br.meetingplace.servicies.notification.Inbox

class EntitiesManagement: GeneralEntitiesManagement() {

    //USERS
    fun follow(data: Follower){

        if(getLoggedUser() != -1){
            val indexExternal = getUserIndex(data.external)
            val indexCurrent = getUserIndex(getLoggedUser())
            val notification = Inbox("${userList[indexCurrent].social.userName} is now following you.", "New follower.")

            if(indexExternal != -1 && verifyFollower(data) == 0){ // verifies if the user you want to follow exists
                userList[indexExternal].social.updateInbox(notification)
                userList[indexExternal].social.followers.add(getLoggedUser())
                userList[indexCurrent].social.following.add(data.external)
            }

        }
    }

    fun unfollow(data: Follower){

        val logged = getLoggedUser()
        if(logged != -1){
            val indexExternal = getUserIndex(data.external)
            val indexCurrent = getUserIndex(logged)

            if( indexCurrent != -1 && indexExternal != -1){
                userList[indexCurrent].social.following.remove(data.external)
                userList[indexExternal].social.followers.remove(logged)
            }
        }
    }

    fun messengerUser(chat: Conversation){

        val indexReceiver = getUserIndex(chat.receiver)
        val logged = getLoggedUser()
        if(logged != -1 && indexReceiver != -1 && chat.receiver != logged){
            val indexSender = getUserIndex(logged)
            val chatId = userList[indexReceiver].getId() + userList[indexSender].getId()

            if(userList[indexReceiver].social.getChatIndex(chatId) != -1){ // The conversation already exists
                val notification = Inbox("${userList[indexSender].social.userName} sent a new message.", "Message.")
                chat.message+=" - "+userList[indexSender].social.userName
                userList[indexReceiver].social.updateChat(chat, chatId)
                userList[indexReceiver].social.updateInbox(notification)
            }
            else { // The conversation doesn't exist
                val notification = Inbox("${userList[indexSender].social.userName} started a conversation with you.", "Message.")
                val newChat = Chat(chatId)
                newChat.conversation.add(chat.message+" - "+userList[indexSender].social.userName)

                userList[indexReceiver].social.startChat(newChat)
                userList[indexReceiver].social.updateInbox(notification)
                userList[indexSender].social.startChat(newChat)
            }
        }
    }

    fun joinGroup(member: UserMember){

        val logged = getLoggedUser()
        if( logged != -1){

            val indexGroup = getGroupIndex(member.group)
            if(indexGroup != -1)
                addMember(member)
        }
    }

    fun leaveGroup(member: UserMember){

        val logged = getLoggedUser()
        if( logged != -1){

            val indexGroup = getGroupIndex(member.group)

            if(indexGroup != -1 && groupList[indexGroup].getCreator() != logged)
                removeMember(member)
        }
    }
    //USERS

    //THREADS
    fun createMainThread(content: ThreadContent){
        if(getLoggedUser() != -1){
            val thread = MainThread()
            val indexUser = getUserIndex(getLoggedUser())
            val id = generateThreadId(userList[indexUser].social.getThreads())
            println(content.body)
            thread.startThread(content, id, userList[indexUser].social.userName, getLoggedUser())
            println(thread.getContent())
            userList[indexUser].social.addNewThread(thread)
        }

    }

    fun deleteThread(operations: Operations){

        if(getLoggedUser() != -1 && operations.pass == cachedPass){
            val indexThread = getThreadIndex(operations.id)
            val indexUser = getUserIndex(getLoggedUser())

            userList[indexUser].social.removeThread(indexThread)
        }

    }

    fun createSubThread(content: ThreadContent){
        println("THICC CODE")
    }
    //THREADS

    //GROUPS
    fun messengerGroup(conversation: GroupConversation){

        val logged = getLoggedUser()
        val indexGroup = getGroupIndex(conversation.group)
        if(logged != -1 && indexGroup != -1){
            val indexUser = getUserIndex(logged)
            groupList[indexGroup].sendMsg(conversation.message +" - " + userList[indexUser].social.userName, logged)
        }

    }

    fun addMember(member: UserMember){

        val indexGroup = getGroupIndex(member.group)
        val indexUser = getUserIndex(member.id)
        val mem = Member(member.id, 0)
        val logged = getLoggedUser()

        if(indexGroup != -1 && indexUser != -1 && member.id != logged && logged != -1){
            // checks if the logged user is on the group
            val indexMember = getMemberIndex(member.id, member.group)
            if(indexMember == -1) // isnt part of the group then add
                groupList[indexGroup].members.add(mem)

        }
    }

    fun removeMember(member: UserMember){

        val user = member.id
        val indexGroup = getGroupIndex(member.group)
        val indexUser = getUserIndex(user)
        val indexMember = getMemberIndex(user, member.group)
        val logged = getLoggedUser()
        // checks if the logged user is on the group and is an admin or the creator
        if(indexGroup != -1 && indexUser != -1 && user == logged && indexMember != -1 && groupList[indexGroup].members[indexMember].role == 1 && logged != -1)
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