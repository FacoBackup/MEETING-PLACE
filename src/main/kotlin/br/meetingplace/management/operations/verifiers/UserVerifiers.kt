package br.meetingplace.management.operations.verifiers

import br.meetingplace.data.entities.user.Follower
import br.meetingplace.entities.user.User
import br.meetingplace.management.GeneralManagement
import br.meetingplace.management.operations.ReadWrite.ReadWrite
import br.meetingplace.management.operations.finders.UserFinders


class UserVerifiers private constructor(){
    val management = GeneralManagement.getLoggedUser()

    companion object{
        private val verifier = UserVerifiers()
        fun getUserVerifier () = verifier
    }


    fun verifyUser(id: String): Boolean {
        if(management != ""){
            //READING
            val rw = ReadWrite.getRW()
            val userList = mutableListOf<User>()
            repeat(userList.size) {
                rw.readUser()
            }
            //READING
            for(i in 0 until userList.size){
                if(userList[i].getId() == id)
                    return true
            }
            return false
        }
        return false
    }

    fun verifyUserSocialProfile(id: String): Boolean {

        val finder = UserFinders.getUserFinder()

        return if(verifyUser(id) && management != ""){

            //READING
            val indexUser = finder.getUserIndex(id)
            val rw = ReadWrite.getRW()
            val userList = mutableListOf<User>()

            repeat(userList.size) {
                rw.readUser()
            }
            //READING

            userList[indexUser].social.getUserName() != ""
        } else false
    }

    fun verifyUserEmail(email: String): Boolean {

        if(management != ""){
            //READING
            val rw = ReadWrite.getRW()
            val userList = mutableListOf<User>()
            repeat(userList.size) {
                rw.readUser()
            }
            //READING

            for(i in 0 until userList.size){
                if(userList[i].getEmail() == email)
                    return true
            }
            return false
        }
        return false
    }

    fun verifyFollower(data: Follower): Boolean {

        if(verifyUser(data.external) && management != ""){
        val finder = UserFinders.getUserFinder()
            val indexExternalUser = finder.getUserIndex(data.external)
            val indexUser = finder.getUserIndex(management)

            //READING
            val rw = ReadWrite.getRW()
            val userList = mutableListOf<User>()

            repeat(userList.size) {
                rw.readUser()
            }
            //READING

            for(i in 0 until userList[indexExternalUser].social.followers.size){
                if(userList[indexExternalUser].social.followers[i] == management)
                    return true
            }
            return false
        }
        return false
    }
}