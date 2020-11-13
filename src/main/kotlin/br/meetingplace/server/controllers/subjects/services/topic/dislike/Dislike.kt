package br.meetingplace.server.controllers.subjects.services.topic.dislike

import br.meetingplace.server.controllers.dependencies.rw.controller.RWController
import br.meetingplace.server.controllers.dependencies.verify.controller.VerifyController
import br.meetingplace.server.dto.topics.TopicOperationsData
import br.meetingplace.server.subjects.services.topic.Topic

class Dislike private constructor() : DislikeInterface {
    private val rw = RWController.getClass()
    private val verify = VerifyController.getClass()

    companion object {
        private val Class = Dislike()
        fun getClass() = Class
    }

    override fun dislike(data: TopicOperationsData) {
        val user = rw.readUser(data.login.email)

        if (verify.verifyUser(user)) {
            when (data.communityID.isNullOrBlank()) {
                true -> { //USER
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> { //MAIN
                            dislikeUserMainTopic(data)
                        }
                        false -> { //SUB
                            dislikeUserSubTopic(data)
                        }
                    }
                }
                false -> { //COMMUNITY
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> { //MAIN
                            dislikeCommunityMainTopic(data)
                        }
                        false -> { //SUB
                            dislikeCommunitySubtopic(data)
                        }
                    }
                }
            }//WHEN
        }//IF VERIFY USER
    }


    private fun dislikeUserMainTopic(data: TopicOperationsData) {
        val topic = rw.readTopic(data.identifier.mainTopicID, data.identifier.mainTopicOwner, null, false)

        if (verify.verifyTopic(topic)) {
            when (checkLikeDislike(topic, data.login.email)) {
                0 -> {
                    println("1")
                    topic.likeToDislike(data.login.email)
                    rw.writeTopic(topic)
                } // like to dislike
                1 -> {
                    println("2")
                    topic.removeDislike(data.login.email)
                    rw.writeTopic(topic)
                }
                2 -> {
                    println("3")
                    topic.dislike(data.login.email)
                    rw.writeTopic(topic)
                }
            }
        }
    }

    private fun dislikeUserSubTopic(data: TopicOperationsData) {
        val topic = data.identifier.subTopicID?.let { rw.readTopic(it, data.identifier.mainTopicOwner, data.identifier.mainTopicID, false) }

        if (topic != null && verify.verifyTopic(topic)) {
            when (checkLikeDislike(topic, data.login.email)) {
                0 -> {
                    topic.likeToDislike(data.login.email)
                    rw.writeTopic(topic)
                } // like to dislike
                1 -> {
                    topic.removeDislike(data.login.email)
                    rw.writeTopic(topic)
                }
                2 -> {
                    topic.dislike(data.login.email)
                    rw.writeTopic(topic)
                }
            }
        }
    }

    private fun dislikeCommunityMainTopic(data: TopicOperationsData) {
        val topic = rw.readTopic(data.identifier.mainTopicID, data.communityID!!, null, true)
        val community = rw.readCommunity(data.communityID)
        if (verify.verifyTopic(topic)
                && verify.verifyCommunity(community) && community.checkTopicApproval(topic.getID())) {
            when (checkLikeDislike(topic, data.login.email)) {
                0 -> {
                    topic.likeToDislike(data.login.email)
                    rw.writeTopic(topic)
                } // like to dislike
                1 -> {
                    topic.removeDislike(data.login.email)
                    rw.writeTopic(topic)
                }
                2 -> {
                    topic.dislike(data.login.email)
                    rw.writeTopic(topic)
                }
            }
        }
    }

    private fun dislikeCommunitySubtopic(data: TopicOperationsData) {
        val topic = data.identifier.subTopicID?.let { rw.readTopic(it, data.communityID!!, data.identifier.mainTopicID, true) }
        val community = rw.readCommunity(data.communityID!!)

        if (topic != null && verify.verifyTopic(topic)
                && verify.verifyCommunity(community) && community.checkTopicApproval(topic.getID())) {
            when (checkLikeDislike(topic, data.login.email)) {
                0 -> {
                    topic.likeToDislike(data.login.email)
                    rw.writeTopic(topic)
                } // like to dislike
                1 -> {
                    topic.removeDislike(data.login.email)
                    rw.writeTopic(topic)
                }
                2 -> {
                    topic.dislike(data.login.email)
                    rw.writeTopic(topic)
                }
            }
        }
    }

    private fun checkLikeDislike(topic: Topic, user: String): Int {
        return when (user) {
            in topic.getLikes() // 0 ALREADY LIKED
            -> 0
            in topic.getDislikes() // 1 ALREADY DISLIKED
            -> 1
            else -> 2 // 2 hasn't DISLIKED or liked yet
        }
    }
}