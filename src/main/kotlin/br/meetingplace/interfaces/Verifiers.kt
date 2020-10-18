package br.meetingplace.interfaces

import br.meetingplace.data.entities.user.Follower
import java.io.File

interface Verifiers: Refresh, Path{

    fun verifyFollower(data: Follower): Boolean {

        val log = readLoggedUser()
        val management = log.user
        if(verifyPath("users", management) && verifyPath("users", data.external) && management != ""){

            val userFollower = readUser(data.external)
            if(management in userFollower.social.followers)
                return true
        }
        return false
    }

    fun verifyUserSocialProfile(id: String): Boolean {

        val log = readLoggedUser()
        val management = log.user

        if(verifyPath("users", management) && management != ""){
            val user = readUser(management)
            if(user.social.getUserName() != "")
                return true
        }
        return false
    }
}