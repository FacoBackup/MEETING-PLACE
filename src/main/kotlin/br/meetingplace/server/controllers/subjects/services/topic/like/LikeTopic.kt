package br.meetingplace.server.controllers.subjects.services.topic.like

import br.meetingplace.server.controllers.readwrite.community.CommunityRWInterface
import br.meetingplace.server.controllers.readwrite.topic.TopicRWInterface
import br.meetingplace.server.controllers.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.topics.TopicOperationsData
import br.meetingplace.server.subjects.services.notification.NotificationData
import br.meetingplace.server.subjects.services.topic.Topic

class LikeTopic private constructor() {
    companion object {
        private val Class = LikeTopic()
        fun getClass() = Class
    }

    fun like(data: TopicOperationsData, rwUser: UserRWInterface, rwTopic: TopicRWInterface, rwCommunity: CommunityRWInterface) {
        val user = rwUser.read(data.login.email)

        if (user != null) {
            when (data.communityID.isNullOrBlank()) {
                true -> { //USER
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> { //MAIN
                            likeUserMainTopic(data,rwUser =  rwUser,rwTopic =  rwTopic)
                        }
                        false -> { //SUB
                            likeUserSubTopic(data,rwUser =  rwUser,rwTopic =  rwTopic)
                        }
                    }
                }
                false -> { //COMMUNITY
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> { //MAIN
                            likeCommunityMainTopic(data,rwUser =  rwUser,rwCommunity =  rwCommunity,rwTopic =  rwTopic)
                        }
                        false -> { //SUB
                            likeCommunitySubtopic(data,rwUser =  rwUser,rwCommunity =  rwCommunity,rwTopic =  rwTopic)
                        }
                    }
                }
            }//WHEN
        }//IF VERIFY USER
    }

    private fun likeUserMainTopic(data: TopicOperationsData, rwUser: UserRWInterface, rwTopic: TopicRWInterface) {
        val user = rwUser.read(data.login.email)
        val topic = rwTopic.read(data.identifier.mainTopicID, null)
        val creator = rwUser.read(data.identifier.mainTopicOwner)
        lateinit var notification: NotificationData

        if (user != null && topic != null && creator != null) {
            notification = NotificationData("${user.getUserName()} liked your reply.", "Thread.")
            when (checkLikeDislike(topic, data.login.email)) {
                0 -> {
                    topic.removeLike(data.login.email)
                    rwTopic.write(topic)
                }
                1 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rwUser.write(creator)
                    }
                    topic.dislikeToLike(data.login.email)
                    rwTopic.write(topic)
                } // like to dislike
                2 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rwUser.write(creator)
                    }
                    topic.like(data.login.email)
                    rwTopic.write(topic)
                } // hasn't DISLIKED yet
            }
        }
    }

    private fun likeUserSubTopic(data: TopicOperationsData, rwUser: UserRWInterface, rwTopic: TopicRWInterface) {
        val user = rwUser.read(data.login.email)
        val subTopic = data.identifier.subTopicID?.let { rwTopic.read(it, data.identifier.mainTopicID) }
        val creator = rwUser.read(data.identifier.mainTopicOwner)
        lateinit var notification: NotificationData

        if (subTopic != null && user != null && creator != null) {
            notification = NotificationData("${user.getUserName()} liked your reply.", "Thread.")
            when (checkLikeDislike(subTopic, data.login.email)) {
                0 -> {
                    subTopic.removeLike(data.login.email)
                    rwTopic.write(subTopic)
                }
                1 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rwUser.write(creator)
                    }
                    subTopic.dislikeToLike(data.login.email)
                    rwTopic.write(subTopic)
                } // like to dislike
                2 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rwUser.write(creator)
                    }
                    subTopic.like(data.login.email)
                    rwTopic.write(subTopic)
                } // hasn't DISLIKED yet
            }
        }
    }

    private fun likeCommunityMainTopic(data: TopicOperationsData, rwTopic: TopicRWInterface, rwUser: UserRWInterface, rwCommunity: CommunityRWInterface) {
        val user = rwUser.read(data.login.email)
        val topic = rwTopic.read(data.identifier.mainTopicID, null)
        val creator = rwUser.read(data.identifier.mainTopicOwner)
        val community = data.communityID?.let { rwCommunity.read(it) }
        lateinit var notification: NotificationData

        if (topic != null && community != null && user != null && creator != null && community.checkTopicApproval(topic.getID())) {
            notification = NotificationData("${user.getUserName()} liked your reply.", "Thread.")
            when (checkLikeDislike(topic, data.login.email)) {
                0 -> {
                    topic.removeLike(data.login.email)
                    rwTopic.write(topic)
                }
                1 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rwUser.write(creator)
                    }
                    topic.dislikeToLike(data.login.email)
                    rwTopic.write(topic)
                } // like to dislike
                2 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rwUser.write(creator)
                    }
                    topic.like(data.login.email)
                    rwTopic.write(topic)
                } // hasn't DISLIKED yet
            }
        }
    }

    private fun likeCommunitySubtopic(data: TopicOperationsData, rwUser: UserRWInterface, rwCommunity: CommunityRWInterface, rwTopic: TopicRWInterface) {
        val user = rwUser.read(data.login.email)
        val subTopic = data.identifier.subTopicID?.let { rwTopic.read(it, data.identifier.mainTopicID) }
        val creator = rwUser.read(data.identifier.mainTopicOwner)
        val community = data.communityID?.let { rwCommunity.read(it) }
        lateinit var notification: NotificationData

        if (subTopic != null && community != null && user != null && creator != null && community.checkTopicApproval(subTopic.getID())) {
            notification = NotificationData("${user.getUserName()} liked your reply.", "Thread.")
            when (checkLikeDislike(subTopic, data.login.email)) {
                0 -> {
                    subTopic.removeLike(data.login.email)
                    rwTopic.write(subTopic)
                }
                1 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rwUser.write(creator)
                    }
                    subTopic.dislikeToLike(data.login.email)
                    rwTopic.write(subTopic)
                } // like to dislike
                2 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rwUser.write(creator)
                    }
                    subTopic.like(data.login.email)
                    rwTopic.write(subTopic)
                } // hasn't DISLIKED yet
            }
        }
    }

    private fun checkLikeDislike(topic: Topic, user: String): Int {// IF TRUE THE USER ALREADY LIKED OR DISLIKED THE THREAD
        return when (user) {
            in topic.getLikes() // 0 ALREADY LIKED
            -> 0
            in topic.getDislikes() // 1 ALREADY DISLIKED
            -> 1
            else -> 2 // 2 hasn't DISLIKED or liked yet
        }
    }
}