package br.meetingplace.management.dependencies.verify.dependencies.user

import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.services.entitie.User
import br.meetingplace.services.entitie.profiles.followdata.FollowData

class UserVerify private constructor(): UserVerifyInterface{
    companion object{
        private val Class= UserVerify()
        fun getClass ()= Class
    }

    override fun verifyUser(user: User): Boolean {
        return  user.getPassword().isNotBlank() && user.getEmail().isNotBlank() && user.getAge() >= 16
    }

    override fun verifyFollower(external: User, user: User): Boolean {
        if(verifyUser(user) && verifyUser(external)){
            if(FollowData(user.getUserName(), user.getEmail()) in external.getFollowers())
                return true
        }
        return false
    }
}