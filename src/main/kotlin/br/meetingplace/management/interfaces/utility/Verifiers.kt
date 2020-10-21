package br.meetingplace.management.interfaces.utility

import br.meetingplace.data.user.Follower

interface Verifiers: Refresh, Path {

    fun verifyFollower(data: Follower): Boolean {
        val management = readLoggedUser().email
        if(verifyPath("users", management) && verifyPath("users", data.external) && management != ""){

            val userFollower = readUser(data.external)
            if(management in userFollower.social.getFollowers())
                return true
        }
        return false
    }

    fun verifyUserSocialProfile(): Boolean {
        val management = readLoggedUser().email
        if(verifyPath("users", management) && management != ""){
            val user = readUser(management)
            if(user.social.getUserName() != "")
                return true
        }
        return false
    }
}