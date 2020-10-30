package br.meetingplace.management.services.user.core

import br.meetingplace.data.Data
import br.meetingplace.data.user.SocialProfileData
import br.meetingplace.data.user.UserData
import br.meetingplace.management.services.user.dependencies.follow.FollowInterface
import br.meetingplace.management.services.user.dependencies.follow.Follow
import br.meetingplace.management.services.user.dependencies.profile.ProfileInterface
import br.meetingplace.management.services.user.dependencies.profile.Profile
import br.meetingplace.management.services.user.dependencies.user.UserFactory
import br.meetingplace.management.services.user.dependencies.user.UserInterface
import br.meetingplace.management.services.user.dependencies.reader.UserReader
import br.meetingplace.management.services.user.dependencies.reader.UserReaderInterface
import br.meetingplace.services.community.Community
import br.meetingplace.services.entitie.User
import br.meetingplace.services.thread.MainThread

class UserCore private constructor(): FollowInterface, UserInterface, ProfileInterface, UserReaderInterface{
    private val profile = Profile.getClass()
    private val user = UserFactory.getClass()
    private val follow = Follow.getClass()
    private val reader = UserReader.getClass()

    companion object{
        private val Class = UserCore()
        fun getClass() = Class
    }

    //USER
    override fun create(newUser: UserData) {
        user.create(newUser)
    }
    override fun delete() {
        user.delete()
    }

    //PROFILE
    override fun createProfile(newProfile: SocialProfileData) {
        profile.createProfile(newProfile)
    }
    override fun clearNotifications() {
        profile.clearNotifications()
    }

    //FOLLOW
    override fun follow(data: Data) {
        follow.follow(data)
    }
    override fun unfollow(data: Data) {
        follow.unfollow(data)
    }

    //READER
    override fun getMyThreads(): List<MainThread> {
        return reader.getMyThreads()
    }
    override fun getMyTimeline(): List<MainThread> {
        return reader.getMyTimeline()
    }
    override fun getMyUser(): User {
        return reader.getMyUser()
    }
}