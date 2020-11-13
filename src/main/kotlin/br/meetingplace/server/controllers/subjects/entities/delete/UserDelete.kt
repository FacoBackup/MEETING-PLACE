package br.meetingplace.server.controllers.subjects.entities.delete

import br.meetingplace.server.controllers.dependencies.newRW.topic.main.TopicRWInterface
import br.meetingplace.server.controllers.dependencies.newRW.user.UserRWInterface
import br.meetingplace.server.dto.Login
import br.meetingplace.server.subjects.entities.User

class UserDelete private constructor() : UserDeleteInterface {
    companion object {
        private val Class = UserDelete()
        fun getClass() = Class
    }

    override fun delete(data: Login, rwUser: UserRWInterface, rwTopic: TopicRWInterface) {
        val user = rwUser.read(data.email)

        lateinit var followers: List<String>
        lateinit var following: List<String>
        lateinit var userExternal: User

        if (user.getEmail().isNotBlank() && data.email == user.getEmail() && data.password == user.getPassword()) {
            followers = user.getFollowers()
            following = user.getFollowing()

            for (index in followers.indices) {
                userExternal = rwUser.read(followers[index])
                if (userExternal.getAge() != -1) {
                    userExternal.updateFollowing(user.getEmail(), true)
                    rwUser.write(userExternal)
                }
            }

            for (index in following.indices) {
                userExternal = rwUser.read(following[index])
                if (userExternal.getAge() != -1) {
                    userExternal.updateFollowers(user.getEmail(), true)
                    rwUser.write(userExternal)
                }
            }
            /*
                for(i in 0 until groupList.size){
                    member = UserMember(management,groupList[i].getEmail())
                    removeMember(member) // should use an override here
                }
             */

            rwUser.delete(user)
            deleteAllTopicsFromUser(data, rwUser = rwUser, rwTopic= rwTopic)
        }
    }

    private fun deleteAllTopicsFromUser(data: Login, rwUser: UserRWInterface, rwTopic: TopicRWInterface) {
        val user = rwUser.read(data.email)

        if (user.getEmail().isNotBlank()) {
            val myTopics = user.getMyTopics()
            for (element in myTopics) {
                val topic = rwTopic.read(element)
                if (topic.getID().isNotBlank())
                    rwTopic.delete(topic)
            }
        }
    } //DELETE
}