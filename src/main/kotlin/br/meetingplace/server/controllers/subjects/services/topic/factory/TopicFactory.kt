package br.meetingplace.server.controllers.subjects.services.topic.factory

import br.meetingplace.server.controllers.dependencies.id.controller.IDController
import br.meetingplace.server.controllers.dependencies.rw.controller.RWController
import br.meetingplace.server.controllers.dependencies.verify.controller.VerifyController
import br.meetingplace.server.dto.topics.TopicData
import br.meetingplace.server.subjects.services.members.data.MemberType
import br.meetingplace.server.subjects.services.owner.OwnerType
import br.meetingplace.server.subjects.services.owner.topic.TopicOwnerData
import br.meetingplace.server.subjects.services.topic.SimplifiedTopic
import br.meetingplace.server.subjects.services.topic.Topic

class TopicFactory private constructor() : TopicFactoryInterface {
    private val iDs = IDController.getClass()
    private val rw = RWController.getClass()
    private val verify = VerifyController.getClass()

    companion object {
        private val Class = TopicFactory()
        fun getClass() = Class
    }

    override fun create(data: TopicData) {
        val user = rw.readUser(data.login.email)

        if (verify.verifyUser(user)) {
            when (data.communityID.isNullOrBlank()) {
                true -> { //USER
                    when (data.identifier == null) {
                        true -> { //MAIN
                            createUserMainTopic(data)
                        }
                        false -> { //SUB
                            createUserSubTopic(data)
                        }
                    }
                }
                false -> { //COMMUNITY
                    when (data.identifier == null) {
                        true -> { //MAIN
                            createCommunityMainTopic(data)
                        }
                        false -> { //SUB
                            createCommunitySubTopic(data)
                        }
                    }
                }
            }//WHEN
        }//IF VERIFY USER
    }

    private fun createCommunityMainTopic(data: TopicData) {
        val user = rw.readUser(data.login.email)
        val community = rw.readCommunity(data.communityID!!)

        if (data.identifier != null && verify.verifyCommunity(community)) {
            val topic = Topic(TopicOwnerData(community.getID(), data.login.email, OwnerType.COMMUNITY), user.getEmail(), iDs.generateId(), null)

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
            rw.writeTopic(topic)
            rw.writeUser(user, user.getEmail())
            rw.writeCommunity(community, community.getID())
        }
    }

    private fun createCommunitySubTopic(data: TopicData) {
        val user = rw.readUser(data.login.email)
        val community = rw.readCommunity(data.communityID!!)

        if (data.identifier != null && verify.verifyCommunity(community)) {
            val mainTopic = rw.readTopic(data.identifier.mainTopicID, data.communityID, null, true)
            if (verify.verifyTopic(mainTopic) && community.checkTopicApproval(mainTopic.getID())) {
                val subtopic = Topic(mainTopic.getOwner(), user.getEmail(), iDs.generateId(), data.identifier.mainTopicID)
                mainTopic.addSubTopic(subtopic.getID())
                subtopic.addContent(data.header, data.body, user.getUserName())
                rw.writeTopic(mainTopic)
                rw.writeTopic(subtopic)
                rw.writeUser(user, user.getEmail())
            }
        }
    }

    private fun createUserMainTopic(data: TopicData) {
        val user = rw.readUser(data.login.email)
        val topic = Topic(TopicOwnerData(user.getEmail(), user.getEmail(), OwnerType.USER), user.getEmail(), iDs.generateId(), null)
        val simplifiedTopic = SimplifiedTopic(topic.getID(), topic.getOwner())

        topic.addContent(data.header, data.body, user.getUserName())
        rw.writeTopic(topic)
        user.updateMyTopics(simplifiedTopic, true)
        rw.writeUser(user, user.getEmail())

    }

    private fun createUserSubTopic(data: TopicData) {
        val user = rw.readUser(data.login.email)

        if (data.identifier != null) {
            val mainTopic = rw.readTopic(data.identifier.mainTopicID, data.identifier.mainTopicOwner, null, false)
            if (verify.verifyTopic(mainTopic)) {
                val subtopic = Topic(mainTopic.getOwner(), user.getEmail(), iDs.generateId(), data.identifier.mainTopicID)
                mainTopic.addSubTopic(subtopic.getID())
                subtopic.addContent(data.header, data.body, user.getUserName())
                rw.writeTopic(mainTopic)
                rw.writeTopic(subtopic)
                rw.writeUser(user, user.getEmail())
            }
        }
    }

}