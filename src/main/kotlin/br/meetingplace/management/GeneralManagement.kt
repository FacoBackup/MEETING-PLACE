package br.meetingplace.management

import br.meetingplace.data.startup.LoginByEmail
import br.meetingplace.data.startup.LoginById
import br.meetingplace.entities.groups.Group
import br.meetingplace.entities.user.User
import br.meetingplace.management.interfaces.Generator
import br.meetingplace.management.operations.ReadWrite.ReadWrite
import br.meetingplace.management.operations.finders.UserFinders
import br.meetingplace.management.operations.verifiers.UserVerifiers

import br.meetingplace.servicies.conversationThread.MainThread
// CHANGE TO INTERFACE
open class GeneralManagement : Generator{

    private var logged = ""
    protected var cachedPass = ""

    companion object{
        private val management = GeneralManagement()
        fun getLoggedUser() = management.logged
    }

    //AUTHENTICATION SYSTEM
    fun loginId(log: LoginById){

        val verifier = UserVerifiers.getUserVerifier()
        val finder = UserFinders.getUserFinder()

        //READING
        val rw = ReadWrite.getRW()
        val userList = mutableListOf<User>()

        repeat(userList.size) {
            rw.readUser()
        }
        //READING

        if(verifier.verifyUser(log.user) && userList[finder.getUserIndex(log.user)].getPassword() == log.password && logged == "") {
            logged = log.user
            cachedPass = log.password
        }
    }

    fun loginEmail(log: LoginByEmail){

        val verifier = UserVerifiers.getUserVerifier()
        val finder = UserFinders.getUserFinder()

        //READING
        val rw = ReadWrite.getRW()
        val userList = mutableListOf<User>()

        repeat(userList.size) {
            rw.readUser()
        }
        //READING


        if(verifier.verifyUserEmail(log.email) && userList[finder.getUserIndexByEmail(log.email)].getPassword() == log.password && logged == "") {
            logged = userList[finder.getUserIndexByEmail(log.email)].getId()
            cachedPass = log.password
        }
    }

    fun logoff(){

        if(logged != ""){
            logged = ""
            cachedPass = ""
        }
    }
    //AUTHENTICATION SYSTEM
}