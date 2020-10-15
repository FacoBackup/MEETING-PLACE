package br.meetingplace.management

import br.meetingplace.data.entities.user.Follower
import br.meetingplace.data.startup.LoginByEmail
import br.meetingplace.data.startup.LoginById
import br.meetingplace.entities.groups.Group
import br.meetingplace.entities.user.User
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.conversationThread.MainThread
import kotlin.random.Random

open class GeneralManagement {


    protected val userList = mutableListOf<User>()
    protected val emailList = mutableListOf<String>()
    protected val nameList = mutableListOf<String>()
    protected val threadList = mutableListOf<MainThread>()
    protected val groupList = mutableListOf<Group>()

    private var logged = -1
    protected var cachedPass = ""
    //GETTERS
    fun getThreads() = threadList
    fun getGroups() = groupList
    fun getUsers() = userList
    fun getUserNameList() = nameList
    fun getEmails() = emailList
    fun getLoggedUser() = logged
    //GETTERS

    //AUTHENTICATION SYSTEM
    fun loginId(log: LoginById){

        val indexUser = getUserIndex(log.user)
        if(verifyUser(log.user) && userList[indexUser].getPassword() == log.password && logged == -1) {
            logged = log.user
            cachedPass = log.password
        }
    }
    fun loginEmail(log: LoginByEmail){

        val indexUser = getUserIndexByEmail(log.email)
        if(logged == -1 && log.email in emailList && userList[indexUser].getPassword() == log.password ) {
            logged = userList[indexUser].getId()
            cachedPass = log.password
        }
    }

    fun logoff(){

        if(logged != -1){
            logged = -1
            cachedPass = ""
        }
    }
    //AUTHENTICATION SYSTEM

    //GENERATORS
    protected fun generateUserId(): Int{
        var id = Random.nextInt(1, 20000)

        for (i in userList){
            while(id == i.getId())
                id = Random.nextInt(1, 20000)
        }
        return id
    }

    protected fun generateMainThreadId(): Int{
        var id = Random.nextInt(1, 20000)

        for (i in 0 until threadList.size){
            while(threadList[i].getId() == id)
                id = Random.nextInt(1, 20000)
        }
        return id
    }

    protected fun generateSubThreadId(thread: MainThread): Int {
        var id = Random.nextInt(1, 20000)

        for (element in thread.getSubThreadsId()){
            while(id == element)
                id = Random.nextInt(1, 20000)
        }
        return id
    }

    protected fun generateGroupId(): Int {
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

    protected fun generateMessageId(chat: Chat): Int {
        var id = Random.nextInt(1, 20000)

        while (id in chat.getMessageIds())
            id = Random.nextInt(1, 20000)

        return id
    }
    //GENERATORS

    //VERIFIERS

    private fun verifyUser(id: Int): Boolean {

        for(i in 0 until userList.size){
            if(userList[i].getId() == id)
                return true
        }
        return false
    }

    protected fun verifyUserSocialProfile(id: Int): Boolean {

        val indexUser = getUserIndex(id)
        return userList[indexUser].social.getUserName() != ""
    }

    protected fun verifyGroupName(name: String): Boolean {
        for(i in 0 until groupList.size){
            if(groupList[i].getNameGroup() == name)
                return false
        }
        return true
    }

    private fun verifyGroup(id: Int): Boolean {

        for(i in 0 until groupList.size){
            if(groupList[i].getId() == id)
                return true
        }
        return false
    }

    protected fun verifyFollower(data: Follower): Int{
        val indexExternal = getUserIndex(data.external)

        for (i in 0 until userList[indexExternal].social.followers.size){
            if(userList[indexExternal].social.followers[i] == logged){
                return 1
            }
        }
        return 0
    }
    //VERIFIERS

    //FINDERS
    protected fun getSocialNameById(id: Int): String{
        if(verifyUser(id)){
            val index = getUserIndex(id)
            return userList[index].social.getUserName()
        }
        return ""
    }

    protected fun getThreadIndex(threadId: Int): Int {
        val indexUser = getUserIndex(getLoggedUser())

        return if(getLoggedUser() != -1 && indexUser != -1){
            for (i in 0 until threadList.size){
                if(threadList[i].getId() == threadId )
                    return i
            }
            -1
        }
        else -1
    }

    protected fun getGroupIndex(id: Int): Int {
        if(verifyGroup(id)){
            for(i in 0 until groupList.size) {
                if (groupList[i].getId() == id)
                    return i
            }
            return -1
        }
        else return -1
    }

    protected fun getUserIndex(id: Int): Int {
        if(verifyUser(id)) {
            for (i in 0 until userList.size) {
                if (userList[i].getId() == id)
                    return i
            }
            return -1
        }
        else return -1
    }

    protected fun getUserIndexByEmail(email: String): Int {
        if(email in emailList) {
            for (i in 0 until emailList.size) {
                if (emailList[i] == email)
                    return i
            }
            return -1
        }
        else return -1
    }
    protected fun getMemberIndex(id: Int, idGroup: Int): Int {
        val indexGroup = getGroupIndex(idGroup)

        if(indexGroup != -1 && groupList[indexGroup].members.size > 0){
            for(i in 0 until groupList[indexGroup].members.size) {
                if (groupList[indexGroup].members[i].user == id)
                    return i
            }
            return -1
        }
        return -1
    }
    //FINDERS
}