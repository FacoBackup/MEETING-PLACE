package br.meetingplace.servicies.management

import br.meetingplace.data.Follower
import br.meetingplace.data.Login
import br.meetingplace.entities.grupos.Group
import br.meetingplace.entities.user.User
import br.meetingplace.servicies.conversationThread.MainThread
import kotlin.random.Random

open class GeneralManagement {

    protected val groupList = mutableListOf<Group>()
    protected val userList = mutableListOf<User>()
    private var logged = -1
    protected var cachedPass = ""
    //GETTERS
    fun getGroups() = groupList
    fun getUsers() = userList
    fun getLoggedUser() = logged
    //GETTERS

    //AUTHENTICATION SYSTEM
    fun login(log: Login){

        val indexUser = getUserIndex(log.user)
        if(verifyUser(log.user) && userList[indexUser].getPass() == log.pass && logged == -1) {
            logged = log.user
            cachedPass = log.pass
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

    protected fun generateMainThreadId(ThreadList: List<Int>): Int{
        var id = Random.nextInt(1, 20000)

        for (element in ThreadList){
            while(id == element)
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
    //GENERATORS

    //VERIFIERS
    protected fun verifyUserName(name: String): Boolean {
        for(i in 0 until userList.size){
            if(userList[i].social.userName == name)
                return false
        }
        return true
    }

    protected fun verifyUser(id: Int): Boolean {

        for(i in 0 until userList.size){
            if(userList[i].getId() == id)
                return true
        }
        return false
    }

    protected fun verifyUserSocialProfile(id: Int): Boolean {

        val indexUser = getUserIndex(id)
        return userList[indexUser].social.userName != ""
    }

    protected fun verifyGroupName(name: String): Boolean {
        for(i in 0 until groupList.size){
            if(groupList[i].getNameGroup() == name)
                return false
        }
        return true
    }

    protected fun verifyGroup(id: Int): Boolean {

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
            return userList[index].social.userName
        }
        return ""
    }

    protected fun getThreadIndex(threadId: Int, userId: Int): Int { // from other users

        val indexUser = getUserIndex(userId)
        return if(getLoggedUser() != -1 && indexUser != -1){
            userList[indexUser].social.getThreadIndex(threadId)
        }
        else -1
    }

    protected fun getThreadIndex(threadId: Int): Int {
        val indexUser = getUserIndex(getLoggedUser())
        return if(getLoggedUser() != -1 && indexUser != -1){

            userList[indexUser].social.getThreadIndex(threadId)
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