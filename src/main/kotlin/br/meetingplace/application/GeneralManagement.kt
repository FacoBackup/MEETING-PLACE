package br.meetingplace.application

import br.meetingplace.entities.grupos.Group
import br.meetingplace.entities.grupos.GroupConversation
import br.meetingplace.entities.grupos.Member
import br.meetingplace.entities.grupos.UserMember
import br.meetingplace.entities.usuario.*
import br.meetingplace.servicies.Authentication
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.notificacao.Inbox
import kotlin.random.Random

open class GeneralManagement() {
    //Listas
    protected val groupList = mutableListOf<Group>()
    protected val userList = mutableListOf<Profile>()
    //Listas

    //Log system
    protected var logged = -1
    private val auto = Authentication()

    fun login(log: Login){
        val indexUser = getIndexUser(log.user)

        if(indexUser != -1 && userList[indexUser].getPass() == log.pass && logged == -1){
            auto.updateLoggedUser(log.user, -1)
            logged = log.user
        }

    }
    protected fun logoff(){
        if(logged != -1){
            auto.updateLoggedUser(-1, logged)
            logged = -1
        }
    }
    //Log system

    //getters
    fun getGroups() = groupList
    fun getUsers() = userList
    fun getUserLogged() = logged
    fun getUserLogged2() = auto.getLoggedUser()
    //getters


    //Generators
    fun generateIdUser(): Int{
        var id = Random.nextInt(1, 20000)

        for (i in userList){
            while(id == i.getId()) // enquanto novo ID existir ele gera diferentes
                id = Random.nextInt(1, 20000)
        }
        return id
    }

    private fun generateIdGroup(): Int {
        var id = Random.nextInt(1, 20000)

        for (i in groupList) {
            if (i.getId() == id) {
                while (id == i.getId()) {
                    id = Random.nextInt(1, 20000)
                }
            }
        }
        return id
    }
    //Generators

    //Verifier
    fun verifyUserName(name: String): Boolean {
        for(i in 0 until userList.size){
            if(userList[i].userName == name)
                return false
        }
        return true
    }
    fun verifyGroupName(name: String): Boolean {
        for(i in 0 until groupList.size){
            if(groupList[i].getNameGroup() == name)
                return false
        }
        return true
    }

    //Verifier
    //Index finders
    fun getIndexGroup(id: Int): Int {
        for(i in 0 until groupList.size) {
            if (groupList[i].getId() == id)
                return i
        }
        return -1
    }

    fun getIndexUser(id: Int): Int {
        for(i in 0 until userList.size) {
            if (userList[i].getId() == id)
                return i
        }
        return -1
    }

    fun getIndexMember(id: Int, idGroup: Int): Int {

        val indexGroup = getIndexGroup(idGroup)

        for(i in 0 until groupList[indexGroup].members.size) {
            if (groupList[indexGroup].members[i].user == id)
                return i
        }
        return -1
    }

    //group related
    open fun message(conversation: GroupConversation){}

    open fun addGroup(group: Group){}

    open fun removeGroup(member: UserMember){}

    open fun addMember(member: UserMember){}

    open fun removeMember(member: UserMember){}
    //group related

    //user related
    open fun addUser(user: Profile){}

    open fun removeUser(User: Int){}

    open fun follow(Friend: Follower){}

    open fun unfollow(Friend: Follower){}

    open fun messenger(chat: Conversation){}

    open fun joinGroup(member: UserMember){}

    open fun leaveGroup(member: UserMember){}
}
