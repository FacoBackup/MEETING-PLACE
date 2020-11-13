package br.meetingplace.server.controllers.subjects.services.topic.delete

import br.meetingplace.server.controllers.dependencies.rw.controller.RWController
import br.meetingplace.server.controllers.dependencies.verify.controller.VerifyController
import br.meetingplace.server.dto.topics.TopicOperationsData
import br.meetingplace.server.subjects.services.members.data.MemberType
import br.meetingplace.server.subjects.services.topic.SimplifiedTopic
import br.meetingplace.server.subjects.services.topic.Topic

class Delete private constructor() : DeleteInterface {
    private val verify = VerifyController.getClass()

    companion object {
        private val Class = Delete()
        fun getClass() = Class
    }

    override fun delete(data: TopicOperationsData) {
        val user = rw.readUser(data.login.email)

        if (verify.verifyUser(user)) {
            when (data.communityID.isNullOrBlank()) {
                true -> { //USER
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> { //MAIN
                            deleteUserMainTopic(data)
                        }
                        false -> { //SUB
                            deleteUserSubTopic(data)
                        }
                    }
                }
                false -> { //COMMUNITY
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> { //MAIN
                            deleteCommunityMainTopic(data)
                        }
                        false -> { //SUB
                            deleteCommunitySubTopic(data)
                        }
                    }
                }
            }//WHEN
        }//IF VERIFY USER
    }//DELETE

    private fun deleteUserMainTopic(data: TopicOperationsData) {
        val user = rw.readUser(data.login.email)
        val topic = rw.readTopic(data.identifier.mainTopicID, data.identifier.mainTopicOwner, null, false)
        lateinit var simplifiedTopic: SimplifiedTopic

        if (verify.verifyTopic(topic) && topic.getCreator() == data.login.email) {
            simplifiedTopic = SimplifiedTopic(topic.getID(), topic.getOwner())
            user.updateMyTopics(simplifiedTopic, false)
            deleteAllUserSubTopics(topic, data.login.email)
            rw.deleteTopic(topic)
            rw.writeUser(user, user.getEmail())
        }
    }

    private fun deleteUserSubTopic(data: TopicOperationsData) {
        val subTopic = data.identifier.subTopicID?.let { rw.readTopic(it, data.identifier.mainTopicOwner, data.identifier.mainTopicID, false) }
        val mainTopic = rw.readTopic(data.identifier.mainTopicID, data.identifier.mainTopicOwner, null, false)
        if (subTopic != null && verify.verifyTopic(subTopic) && subTopic.getCreator() == data.login.email) {
            subTopic.removeSubTopic(data.identifier.subTopicID)
            mainTopic.removeSubTopic(subTopic.getID())
            rw.writeTopic(mainTopic)
            rw.deleteTopic(subTopic)
        }
    }

    private fun deleteCommunityMainTopic(data: TopicOperationsData) {
        val topic = data.identifier.subTopicID?.let { rw.readTopic(it, data.communityID!!, null, true) }
        val community = rw.readCommunity(data.communityID!!)

        if (topic != null && verify.verifyTopic(topic) && verify.verifyCommunity(community) && community.checkTopicApproval(topic.getID()) &&
                (topic.getCreator() == data.login.email || community.getMemberRole(data.login.email) == MemberType.MODERATOR || community.getMemberRole(data.login.email) == MemberType.CREATOR)) {

            community.removeApprovedTopic(SimplifiedTopic(topic.getID(), topic.getOwner()))
            deleteAllCommunitySubTopics(topic, community.getID())
            rw.deleteTopic(topic)
        }
    }

    private fun deleteCommunitySubTopic(data: TopicOperationsData) {
        val subTopic = data.identifier.subTopicID?.let { rw.readTopic(it, data.communityID!!, data.identifier.mainTopicID, true) }
        val mainTopic = rw.readTopic(data.identifier.mainTopicID, data.communityID!!, null, true)
        val community = rw.readCommunity(data.communityID)

        if (subTopic != null && verify.verifyTopic(subTopic) && verify.verifyTopic(mainTopic) &&
                verify.verifyCommunity(community) && community.checkTopicApproval(mainTopic.getID()) &&
                (subTopic.getCreator() == data.login.email || community.getMemberRole(data.login.email) == MemberType.MODERATOR || community.getMemberRole(data.login.email) == MemberType.CREATOR)) {

            subTopic.removeSubTopic(data.identifier.subTopicID)
            mainTopic.removeSubTopic(subTopic.getID())
            rw.writeTopic(mainTopic)
            rw.deleteTopic(subTopic)
        }
    }

    private fun deleteAllUserSubTopics(mainTopic: Topic, owner: String) {
        val subTopics = mainTopic.getSubTopics()
        lateinit var subtopic: Topic

        for (i in subTopics.indices) {
            subtopic = rw.readTopic(subTopics[i], owner, mainTopic.getID(), false)
            rw.deleteTopic(subtopic)
        }
    }

    private fun deleteAllCommunitySubTopics(mainTopic: Topic, communityID: String) {
        val subTopics = mainTopic.getSubTopics()
        lateinit var subtopic: Topic

        for (i in subTopics.indices) {
            subtopic = rw.readTopic(subTopics[i], communityID, mainTopic.getID(), true)
            rw.deleteTopic(subtopic)
        }
    }
}