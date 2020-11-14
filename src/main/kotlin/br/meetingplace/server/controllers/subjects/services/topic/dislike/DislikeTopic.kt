package br.meetingplace.server.controllers.subjects.services.topic.dislike

import br.meetingplace.server.controllers.readwrite.community.CommunityRWInterface
import br.meetingplace.server.controllers.readwrite.topic.TopicRWInterface
import br.meetingplace.server.controllers.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.topics.TopicOperationsData
import br.meetingplace.server.subjects.services.topic.Topic

class DislikeTopic private constructor() {
    companion object {
        private val Class = DislikeTopic()
        fun getClass() = Class
    }

    fun dislike(data: TopicOperationsData, rwUser: UserRWInterface, rwTopic: TopicRWInterface, rwCommunity: CommunityRWInterface) {
        val user = rwUser.read(data.login.email)

        if (user != null) {
            when (data.communityID.isNullOrBlank()) {
                true -> { //USER
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> { //MAIN
                            dislikeUserMainTopic(data, rwTopic)
                        }
                        false -> { //SUB
                            dislikeUserSubTopic(data, rwTopic)
                        }
                    }
                }
                false -> { //COMMUNITY
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> { //MAIN
                            dislikeCommunityMainTopic(data, rwTopic, rwCommunity)
                        }
                        false -> { //SUB
                            dislikeCommunitySubtopic(data, rwTopic, rwCommunity)
                        }
                    }
                }
            }
        }
    }


    private fun dislikeUserMainTopic(data: TopicOperationsData, rwTopic: TopicRWInterface) {
        val topic = rwTopic.read(data.identifier.mainTopicID, null)

        if (topic != null) {
            when (checkLikeDislike(topic, data.login.email)) {
                0 -> {
                    println("1")
                    topic.likeToDislike(data.login.email)
                    rwTopic.write(topic)
                } // like to dislike
                1 -> {
                    println("2")
                    topic.removeDislike(data.login.email)
                    rwTopic.write(topic)
                }
                2 -> {
                    println("3")
                    topic.dislike(data.login.email)
                    rwTopic.write(topic)
                }
            }
        }
    }

    private fun dislikeUserSubTopic(data: TopicOperationsData, rwTopic: TopicRWInterface, ) {
        val subTopic = data.identifier.subTopicID?.let { rwTopic.read(it, data.identifier.mainTopicID) }
        val mainTopic = rwTopic.read(data.identifier.mainTopicID, null)

        if (subTopic != null && mainTopic != null) {
            when (checkLikeDislike(subTopic, data.login.email)) {
                0 -> {
                    subTopic.likeToDislike(data.login.email)
                    rwTopic.write(subTopic)
                } // like to dislike
                1 -> {
                    subTopic.removeDislike(data.login.email)
                    rwTopic.write(subTopic)
                }
                2 -> {
                    subTopic.dislike(data.login.email)
                    rwTopic.write(subTopic)
                }
            }
        }
    }

    private fun dislikeCommunityMainTopic(data: TopicOperationsData, rwTopic: TopicRWInterface, rwCommunity: CommunityRWInterface) {
        val topic = rwTopic.read(data.identifier.mainTopicID, null)
        val community = data.communityID?.let { rwCommunity.read(it) }
        if (topic != null && community != null && community.checkTopicApproval(topic.getID())) {
            when (checkLikeDislike(topic, data.login.email)) {
                0 -> {
                    topic.likeToDislike(data.login.email)
                    rwTopic.write(topic)
                } // like to dislike
                1 -> {
                    topic.removeDislike(data.login.email)
                    rwTopic.write(topic)
                }
                2 -> {
                    topic.dislike(data.login.email)
                    rwTopic.write(topic)
                }
            }
        }
    }

    private fun dislikeCommunitySubtopic(data: TopicOperationsData, rwTopic: TopicRWInterface, rwCommunity: CommunityRWInterface, ) {
        val subTopic = data.identifier.subTopicID?.let { rwTopic.read(it, data.identifier.mainTopicID) }
        val mainTopic = rwTopic.read(data.identifier.mainTopicID, null)

        val community = data.communityID?.let { rwCommunity.read(it) }

        if (subTopic != null && mainTopic != null && community != null && community.checkTopicApproval(mainTopic.getID())) {
            when (checkLikeDislike(subTopic, data.login.email)) {
                0 -> {
                    subTopic.likeToDislike(data.login.email)
                    rwTopic.write(subTopic)
                } // like to dislike
                1 -> {
                    subTopic.removeDislike(data.login.email)
                    rwTopic.write(subTopic)
                }
                2 -> {
                    subTopic.dislike(data.login.email)
                    rwTopic.write(subTopic)
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