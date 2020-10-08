package br.meetingplace.application

import br.meetingplace.entities.grupos.Group
import br.meetingplace.entities.grupos.GroupConversation
import br.meetingplace.entities.grupos.Member
import br.meetingplace.entities.grupos.UserMember

open class GroupManagement:  GeneralManagement(){


    override fun message(conversation: GroupConversation){

        val indexUser = getIndexUser(conversation.sender)
        val indexGroup = getIndexGroup(conversation.group)

        if(conversation.sender == logged && indexGroup != -1 ){

            val indexMember = getIndexMember(conversation.sender, conversation.group)
            if(indexMember != -1){ // If indexMember == 1 then the user isnt part of the group
                conversation.message = conversation.message + userList[indexUser].userName
                groupList[indexGroup].sendMsg(conversation, logged)
            }
        }
    }

    override fun addGroup(group: Group){

        val creator = group.getCreator()
        val indexCreator = getIndexUser(creator)

        if(group.getId() == -1 && verifyGroupName(group.getNameGroup())){
            group.updateId(generateIdUser())
            userList[indexCreator].groups.add(group.getId())
            if(creator != -1 && creator == logged)
                groupList.add(group)
        }
    }

    override fun removeGroup(member: UserMember){

        val indexGroup = getIndexGroup(member.group)

        if(indexGroup != -1 && groupList[indexGroup].getCreator() == member.id && member.id == logged){ //ONLY THE CREATOR OF THE GROUP CAN DELETE IT
            // percorre a lista de usuarios e remove todos que fazem parte do grupo
            for(i in 0 until userList.size)
                userList[i].groups.remove(member.group) // removing the group id from the users profile

            groupList.remove(groupList[indexGroup])
        }
    }

    override fun addMember(member: UserMember){

        val indexGroup = getIndexGroup(member.group)
        val indexUser = getIndexUser(member.id)


        if(indexGroup != -1 && indexUser != -1 && member.id != logged ){
            // checks if the logged user is on the group
            val indexMember = getIndexMember(member.id, member.group)
            if(indexMember == -1){ // isnt part of the group then add
                val mem = Member(member.id, 0)
                groupList[indexGroup].members.add(mem)
            }
        }
    }

    override fun removeMember(member: UserMember){

        val user = member.id
        val indexGroup = getIndexGroup(member.group)
        val indexUser = getIndexUser(user)
        val indexMember = getIndexUser(user)

        // checks if the logged user is on the group and is an admin or the creator
        if(indexGroup != -1 && indexUser != -1 && user == logged && indexMember != -1){
            groupList[indexGroup].members.remove(groupList[indexGroup].members[indexMember])
        }
    }

    /*


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