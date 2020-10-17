package br.meetingplace.management.operations.verifiers

import br.meetingplace.data.entities.user.Follower
import br.meetingplace.interfaces.ReadFile
import br.meetingplace.interfaces.WriteFile
import java.io.File


class UserVerifiers private constructor(): ReadFile, WriteFile{

    private val log = readLoggedUser()
    private val management = log.user

    companion object{
        private val verifier = UserVerifiers()
        fun getUserVerifier () = verifier
    }

    fun verifyUserSocialProfile(id: String): Boolean {
        val fileUser = File("$management.json").exists()
        if(fileUser && management != ""){
            val user = readUser(management)
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
//            readUser()?.let { userList.add(it) }
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
            val userFollower = readUser(data.external)

            if(management in userFollower.social.followers)
                return true
            return false
        }
        return false
    }
}