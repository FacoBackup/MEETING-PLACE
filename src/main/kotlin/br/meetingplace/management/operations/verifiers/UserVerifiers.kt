package br.meetingplace.management.operations.verifiers

import br.meetingplace.data.entities.user.Follower
import br.meetingplace.entities.user.User
import br.meetingplace.management.GeneralManagement
import br.meetingplace.management.operations.ReadWrite.ReadWrite
import java.io.File


class UserVerifiers private constructor(){

    private val verifier = UserVerifiers.getUserVerifier()
    private val rw = ReadWrite.getRW()
    private val system = GeneralManagement.getManagement()
    private val management = system.getLoggedUser()

    companion object{
        private val verifier = UserVerifiers()
        fun getUserVerifier () = verifier
    }

    fun verifyUserSocialProfile(id: String): Boolean {
        val fileUser = File("$management.json").exists()
        if(fileUser && management != ""){
            val user = rw.readUser(management)
            if(user.social.getUserName() != "")
                return true
            return false
        }
        else return false
    }

//    fun verifyUserEmail(email: String): Boolean {
//
//        if(management != ""){
//            //READING
//            val rw = ReadWrite.getRW()
//            val userList = mutableListOf<User>()
//            rw.readUser()?.let { userList.add(it) }
//            //READING
//
//            for(i in 0 until userList.size){
//                if(userList[i].getEmail() == email)
//                    return true
//            }
//            return false
//        }
//        return false
//    }

    fun verifyFollower(data: Follower): Boolean {


        val fileUser = File("$management.json").exists()
        val fileFollower = File("${data.external}.json").exists()
        if(fileUser && fileFollower && management != ""){
            val userFollower = rw.readUser(data.external)

            if(management in userFollower.social.followers)
                return true
            return false
        }
        return false
    }
}