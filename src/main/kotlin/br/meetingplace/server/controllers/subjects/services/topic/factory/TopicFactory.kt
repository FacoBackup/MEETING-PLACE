package br.meetingplace.server.controllers.subjects.services.topic.factory

import br.meetingplace.server.controllers.dependencies.readwrite.community.CommunityRWInterface
import br.meetingplace.server.controllers.dependencies.readwrite.topic.main.TopicRWInterface
import br.meetingplace.server.controllers.dependencies.readwrite.topic.sub.SubTopicRWInterface
import br.meetingplace.server.controllers.dependencies.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.topics.TopicData
import br.meetingplace.server.subjects.services.members.data.MemberType
import br.meetingplace.server.subjects.services.owner.OwnerType
import br.meetingplace.server.subjects.services.owner.topic.TopicOwnerData
import br.meetingplace.server.subjects.services.topic.SimplifiedTopic
import br.meetingplace.server.subjects.services.topic.Topic
import java.util.*

class TopicFactory private constructor() {
    companion object {
        private val Class = TopicFactory()
        fun getClass() = Class
    }

    fun create(data: TopicData, rwTopic: TopicRWInterface, rwUser: UserRWInterface, rwSubTopic: SubTopicRWInterface, rwCommunity: CommunityRWInterface) {
        val user = rwUser.read(data.login.email)

        if (user != null) {
            when (data.communityID.isNullOrBlank()) {
                true -> { //USER
                    when (data.identifier == null) {
                        true -> { //MAIN
                            createUserMainTopic(data, rwUser, rwTopic)
                        }
                        false -> { //SUB
                            createUserSubTopic(data, rwUser, rwTopic, rwSubTopic)
                        }
                    }
                }
                false -> { //COMMUNITY
                    when (data.identifier == null) {
                        true -> { //MAIN
                            createCommunityMainTopic(data, rwUser, rwCommunity, rwTopic)
                        }
                        false -> { //SUB
                            createCommunitySubTopic(data, rwUser, rwCommunity, rwTopic, rwSubTopic)
                        }
                    }
                }
            }//WHEN
        }//IF VERIFY USER
    }

    private fun createCommunityMainTopic(data: TopicData, rwUser: UserRWInterface, rwCommunity: CommunityRWInterface, rwTopic: TopicRWInterface) {
        val user = rwUser.read(data.login.email)
        val community = data.communityID?.let { rwCommunity.read(it) }

        if (data.identifier != null && community != null && user != null) {
            val topic = Topic(TopicOwnerData(community.getID(), data.login.email, OwnerType.COMMUNITY), user.getEmail(), UUID.randomUUID().toString(), null)

            when (community.getMemberRole(data.login.email)) {
                MemberType.CREATOR -> {
                    community.updateTopicInValidation(SimplifiedTopic(topic.getID(), topic.getOwner()), true)
                }
                MemberType.MODERATOR -> {
                    community.updateTopicInValidation(SimplifiedTopic(topic.getID(), topic.getOwner()), true)
                }
                MemberType.NORMAL -> {
                    community.updateTopicInValidation(SimplifiedTopic(topic.getID(), topic.getOwner()), null)
                }
            }
            rwTopic.write(topic)
            rwUser.write(user)
            rwCommunity.write(community)
        }
    }

    private fun createCommunitySubTopic(data: TopicData, rwUser: UserRWInterface, rwCommunity: CommunityRWInterface, rwTopic: TopicRWInterface, rwSubTopic: SubTopicRWInterface) {
        val user = rwUser.read(data.login.email)
        val community = data.communityID?.let { rwCommunity.read(it) }

        if (data.identifier != null && community != null && user != null) {
            val mainTopic = rwTopic.read(data.identifier.mainTopicID)
            if (mainTopic != null && community.checkTopicApproval(mainTopic.getID())) {
                val subtopic = Topic(mainTopic.getOwner(), user.getEmail(), UUID.randomUUID().toString(), data.identifier.mainTopicID)
                mainTopic.addSubTopic(subtopic.getID())
                subtopic.addContent(data.header, data.body, user.getUserName())
                rwTopic.write(mainTopic)
                rwSubTopic.write(subtopic)
                rwUser.write(user)
            }
        }
    }

    private fun createUserMainTopic(data: TopicData, rwUser: UserRWInterface, rwTopic: TopicRWInterface) {
        val user = rwUser.read(data.login.email)

        if(user != null){
            val topic = Topic(TopicOwnerData(user.getEmail(), user.getEmail(), OwnerType.USER), user.getEmail(), UUID.randomUUID().toString(), null)

            topic.addContent(data.header, data.body, user.getUserName())
            rwTopic.write(topic)
            user.updateMyTopics(topic.getID(), true)
            rwUser.write(user)
        }
    }

    private fun createUserSubTopic(data: TopicData, rwUser: UserRWInterface, rwTopic: TopicRWInterface, rwSubTopic: SubTopicRWInterface) {
        val user = rwUser.read(data.login.email)

        if (data.identifier != null && user != null) {
            val mainTopic = rwTopic.read(data.identifier.mainTopicID)
            if (mainTopic != null) {
                val subtopic = Topic(mainTopic.getOwner(), user.getEmail(), UUID.randomUUID().toString(), data.identifier.mainTopicID)
                mainTopic.addSubTopic(subtopic.getID())
                subtopic.addContent(data.header, data.body, user.getUserName())
                rwTopic.write(mainTopic)
                rwSubTopic.write(subtopic)
                rwUser.write(user)
            }
        }
    }

}