package br.meetingplace.server.controllers.subjects.services.topic.delete

import br.meetingplace.server.controllers.readwrite.community.CommunityRWInterface
import br.meetingplace.server.controllers.readwrite.topic.TopicRWInterface
import br.meetingplace.server.controllers.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.topics.TopicOperationsData
import br.meetingplace.server.subjects.services.members.data.MemberType
import br.meetingplace.server.subjects.services.topic.SimplifiedTopic
import br.meetingplace.server.subjects.services.topic.Topic

class DeleteTopic private constructor() {
    companion object {
        private val Class = DeleteTopic()
        fun getClass() = Class
    }

    fun delete(data: TopicOperationsData, rwUser: UserRWInterface, rwTopic: TopicRWInterface, rwCommunity: CommunityRWInterface) {
        val user = rwUser.read(data.login.email)

        if (user != null) {
            when (data.communityID.isNullOrBlank()) {
                true -> { //USER
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> { //MAIN
                            deleteUserMainTopic(data,rwTopic, rwUser)
                        }
                        false -> { //SUB
                            deleteUserSubTopic(data,rwTopic)
                        }
                    }
                }
                false -> { //COMMUNITY
                    when (data.identifier.subTopicID.isNullOrBlank()) {
                        true -> { //MAIN
                            deleteCommunityMainTopic(data, rwCommunity, rwTopic)
                        }
                        false -> { //SUB
                            deleteCommunitySubTopic(data,rwCommunity, rwTopic)
                        }
                    }
                }
            }//WHEN
        }//IF VERIFY USER
    }//DELETE

    private fun deleteUserMainTopic(data: TopicOperationsData, rwTopic: TopicRWInterface, rwUser: UserRWInterface) {
        val user = rwUser.read(data.login.email)
        val topic = rwTopic.read(data.identifier.mainTopicID, null)

        if (topic != null && user != null && topic.getCreator() == data.login.email) {
            user.updateMyTopics(topic.getID(), false)
            deleteAllSubTopics(topic, rwTopic)
            rwTopic.delete(topic)
            rwUser.write(user)
        }
    }

    private fun deleteUserSubTopic(data: TopicOperationsData, rwTopic: TopicRWInterface) {
        val subTopic = data.identifier.subTopicID?.let { rwTopic.read(it, data.identifier.mainTopicID) }
        val mainTopic = rwTopic.read(data.identifier.mainTopicID, null)
        if (subTopic != null && mainTopic != null && subTopic.getCreator() == data.login.email) {
            subTopic.removeSubTopic(data.identifier.subTopicID)
            mainTopic.removeSubTopic(subTopic.getID())
            rwTopic.write(mainTopic)
            rwTopic.delete(subTopic)
        }
    }

    private fun deleteCommunityMainTopic(data: TopicOperationsData, rwCommunity: CommunityRWInterface, rwTopic: TopicRWInterface) {
        val mainTopic = rwTopic.read(data.identifier.mainTopicID, null)
        val community = data.communityID?.let { rwCommunity.read(it) }

        if (mainTopic!= null && community != null && community.checkTopicApproval(mainTopic.getID()) &&
                (mainTopic.getCreator() == data.login.email || community.getMemberRole(data.login.email) == MemberType.MODERATOR || community.getMemberRole(data.login.email) == MemberType.CREATOR)) {

            community.removeApprovedTopic(SimplifiedTopic(mainTopic.getID(), mainTopic.getOwner()))
            deleteAllSubTopics(mainTopic,rwTopic)
            rwTopic.delete(mainTopic)
        }
    }

    private fun deleteCommunitySubTopic(data: TopicOperationsData, rwCommunity: CommunityRWInterface, rwTopic: TopicRWInterface) {
        val subTopic = data.identifier.subTopicID?.let { rwTopic.read(it, data.identifier.mainTopicID) }
        val mainTopic = rwTopic.read(data.identifier.mainTopicID, null)
        val community = data.communityID?.let { rwCommunity.read(it) }

        if (subTopic != null && mainTopic != null && community != null && community.checkTopicApproval(mainTopic.getID()) &&
                (subTopic.getCreator() == data.login.email || community.getMemberRole(data.login.email) == MemberType.MODERATOR || community.getMemberRole(data.login.email) == MemberType.CREATOR)) {

            subTopic.removeSubTopic(data.identifier.subTopicID)
            mainTopic.removeSubTopic(subTopic.getID())
            rwTopic.write(mainTopic)
            rwTopic.delete(subTopic)
        }
    }

    private fun deleteAllSubTopics(mainTopic: Topic, rwTopic: TopicRWInterface) {
        val subTopics = mainTopic.getSubTopics()

        for (i in subTopics.indices) {
            val subtopic = rwTopic.read(subTopics[i], mainTopic.getID())
            if(subtopic != null)
                rwTopic.delete(subtopic)
        }
    }
}