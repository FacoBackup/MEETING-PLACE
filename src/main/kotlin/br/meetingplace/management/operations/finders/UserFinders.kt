package br.meetingplace.management.operations.finders

import br.meetingplace.entities.user.User
import br.meetingplace.management.GeneralManagement
import br.meetingplace.management.operations.ReadWrite.ReadWrite
import br.meetingplace.management.operations.verifiers.UserVerifiers

class UserFinders {
    val management = GeneralManagement.getLoggedUser()

    companion object{
        private val finder = UserFinders()
        fun getUserFinder () = finder
    }

    fun getUserIndexByEmail(email: String): Int {

        val verifier = UserVerifiers.getUserVerifier()

        if(verifier.verifyUserEmail(email) && management != "") {

            //READING
            val rw = ReadWrite.getRW()
            val userList = mutableListOf<User>()
            repeat(userList.size) {
                rw.readUser()
            }
            //READING

            for (i in 0 until userList.size) {
                if (userList[i].getEmail() == email)
                    return i
            }
            return -1
        }
        else return -1
    }

    fun getUserIndex(id: String): Int {
        val verifier = UserVerifiers.getUserVerifier()
        if(verifier.verifyUser(id) && management != "") {
            //READING
            val rw = ReadWrite.getRW()
            val userList = mutableListOf<User>()
            repeat(userList.size) {
                rw.readUser()
            }
            //READING

            for (i in 0 until userList.size) {
                if (userList[i].getId() == id)
                    return i
            }
            return -1
        }
        else return -1
    }
}