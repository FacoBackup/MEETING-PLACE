package br.meetingplace.server.controllers.subjects.services.topic.like

import br.meetingplace.server.controllers.dependencies.rw.controller.RWController
import br.meetingplace.server.controllers.dependencies.verify.controller.VerifyController
import br.meetingplace.server.dto.topics.TopicOperationsData
import br.meetingplace.server.subjects.services.notification.NotificationData
import br.meetingplace.server.subjects.services.topic.Topic

class Like private constructor() : LikeInterface {
    private val rw = RWController.getClass()
    private val verify = VerifyController.getClass()

    companion object {
        private val Class = Like()
        fun getClass() = Class
    }

    override fun like(data: TopicOperationsData) {
        val user = rw.readUser(data.login.email)

        if (verify.verifyUser(user)) {
            when (data.communityID.isNullOrBlank()) {
                true -> { //USER
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> { //MAIN
                            likeUserMainTopic(data)
                        }
                        false -> { //SUB
                            likeUserSubTopic(data)
                        }
                    }
                }
                false -> { //COMMUNITY
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> { //MAIN
                            likeCommunityMainTopic(data)
                        }
                        false -> { //SUB
                            likeCommunitySubtopic(data)
                        }
                    }
                }
            }//WHEN
        }//IF VERIFY USER
    }

    private fun likeUserMainTopic(data: TopicOperationsData) {
        val user = rw.readUser(data.login.email)
        val topic = rw.readTopic(data.identifier.mainTopicID, data.identifier.mainTopicOwner, null, false)
        val creator = rw.readUser(data.identifier.mainTopicOwner)
        lateinit var notification: NotificationData

        if (verify.verifyTopic(topic) && verify.verifyUser(creator)) {
            notification = NotificationData("${user.getUserName()} liked your reply.", "Thread.")
            when (checkLikeDislike(topic, data.login.email)) {
                0 -> {
                    topic.removeLike(data.login.email)
                    rw.writeTopic(topic)
                }
                1 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rw.writeUser(creator, creator.getEmail())
                    }
                    topic.dislikeToLike(data.login.email)
                    rw.writeTopic(topic)
                } // like to dislike
                2 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rw.writeUser(creator, creator.getEmail())
                    }
                    topic.like(data.login.email)
                    rw.writeTopic(topic)
                } // hasn't DISLIKED yet
            }
        }
    }

    private fun likeUserSubTopic(data: TopicOperationsData) {
        val user = rw.readUser(data.login.email)
        val topic = data.identifier.subTopicID?.let { rw.readTopic(it, data.identifier.mainTopicOwner, data.identifier.mainTopicID, false) }
        val creator = rw.readUser(data.identifier.mainTopicOwner)
        lateinit var notification: NotificationData

        if (topic != null && verify.verifyTopic(topic) && verify.verifyUser(creator)) {
            notification = NotificationData("${user.getUserName()} liked your reply.", "Thread.")
            when (checkLikeDislike(topic, data.login.email)) {
                0 -> {
                    topic.removeLike(data.login.email)
                    rw.writeTopic(topic)
                }
                1 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rw.writeUser(creator, creator.getEmail())
                    }
                    topic.dislikeToLike(data.login.email)
                    rw.writeTopic(topic)
                } // like to dislike
                2 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rw.writeUser(creator, creator.getEmail())
                    }
                    topic.like(data.login.email)
                    rw.writeTopic(topic)
                } // hasn't DISLIKED yet
            }
        }
    }

    private fun likeCommunityMainTopic(data: TopicOperationsData) {
        val user = rw.readUser(data.login.email)
        val topic = rw.readTopic(data.identifier.mainTopicID, data.communityID!!, null, true)
        val creator = rw.readUser(data.identifier.mainTopicOwner)
        val community = rw.readCommunity(data.communityID)
        lateinit var notification: NotificationData

        if (verify.verifyTopic(topic) && verify.verifyUser(creator) && verify.verifyCommunity(community) && community.checkTopicApproval(topic.getID())) {
            notification = NotificationData("${user.getUserName()} liked your reply.", "Thread.")
            when (checkLikeDislike(topic, data.login.email)) {
                0 -> {
                    topic.removeLike(data.login.email)
                    rw.writeTopic(topic)
                }
                1 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rw.writeUser(creator, creator.getEmail())
                    }
                    topic.dislikeToLike(data.login.email)
                    rw.writeTopic(topic)
                } // like to dislike
                2 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rw.writeUser(creator, creator.getEmail())
                    }
                    topic.like(data.login.email)
                    rw.writeTopic(topic)
                } // hasn't DISLIKED yet
            }
        }
    }

    private fun likeCommunitySubtopic(data: TopicOperationsData) {
        val user = rw.readUser(data.login.email)
        val topic = data.identifier.subTopicID?.let { rw.readTopic(it, data.communityID!!, data.identifier.mainTopicID, true) }
        val creator = rw.readUser(data.identifier.mainTopicOwner)
        val community = rw.readCommunity(data.communityID!!)
        lateinit var notification: NotificationData

        if (topic != null && verify.verifyTopic(topic) && verify.verifyUser(creator) && verify.verifyCommunity(community) && community.checkTopicApproval(topic.getID())) {
            notification = NotificationData("${user.getUserName()} liked your reply.", "Thread.")
            when (checkLikeDislike(topic, data.login.email)) {
                0 -> {
                    topic.removeLike(data.login.email)
                    rw.writeTopic(topic)
                }
                1 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rw.writeUser(creator, creator.getEmail())
                    }
                    topic.dislikeToLike(data.login.email)
                    rw.writeTopic(topic)
                } // like to dislike
                2 -> {
                    if (user.getEmail() != creator.getEmail()) {
                        creator.updateInbox(notification)
                        rw.writeUser(creator, creator.getEmail())
                    }
                    topic.like(data.login.email)
                    rw.writeTopic(topic)
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