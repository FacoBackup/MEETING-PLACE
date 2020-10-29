package br.meetingplace.management.services.user.core

import br.meetingplace.data.Data
import br.meetingplace.data.user.SocialProfileData
import br.meetingplace.data.user.UserData
import br.meetingplace.management.services.user.dependencies.follow.FollowInterface
import br.meetingplace.management.services.user.dependencies.follow.FollowOperator
import br.meetingplace.management.services.user.dependencies.profile.ProfileInterface
import br.meetingplace.management.services.user.dependencies.profile.ProfileOperator
import br.meetingplace.management.services.user.dependencies.user.UserFactory
import br.meetingplace.management.services.user.dependencies.user.UserInterface
import br.meetingplace.management.services.user.dependencies.user.UserReader

class UserCore private constructor(): FollowInterface, UserInterface, ProfileInterface, UserReader(){

    companion object{
        private val Class = UserCore()
        fun getCore() = Class
    }

    //USER
    override fun create(newUser: UserData) {
        UserFactory.getFactory().create(newUser)
    }
    override fun delete() {
        UserFactory.getFactory().delete()
    }

    //PROFILE
    override fun createProfile(newProfile: SocialProfileData) {
        ProfileOperator.getProfileOperator().createProfile(newProfile)
    }
    override fun clearNotifications() {
        ProfileOperator.getProfileOperator().clearNotifications()
    }

    //FOLLOW
    override fun follow(data: Data) {
        FollowOperator.getFollowOperator().follow(data)
    }
    override fun unfollow(data: Data) {
        FollowOperator.getFollowOperator().unfollow(data)
    }

}