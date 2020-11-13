package br.meetingplace.server.subjects.services.topic.dependencies

import br.meetingplace.server.subjects.services.topic.dependencies.content.TopicContent
import br.meetingplace.server.subjects.services.topic.dependencies.content.TopicContentInterface
import br.meetingplace.server.subjects.services.topic.dependencies.opinions.TopicOpinion
import br.meetingplace.server.subjects.services.topic.dependencies.opinions.TopicOpinionInterface
import br.meetingplace.server.subjects.services.topic.dependencies.sub.SubTopic
import br.meetingplace.server.subjects.services.topic.dependencies.sub.SubTopicInterface

abstract class Controller : TopicContentInterface, TopicOpinionInterface, SubTopicInterface {
    private val opinions = TopicOpinion.getClass()
    private val content = TopicContent.getClass()
    private val subTopics = SubTopic.getClass()

    //CONTENT
    override fun addContent(header: String, body: String, footer: String) {
        content.addContent(header, body, footer)
    }

    override fun editBody(body: String) {
        content.editBody(body)
    }

    override fun editHeader(header: String) {
        content.editHeader(header)
    }

    //SUBTOPIC
    override fun addSubTopic(subTopicID: String) {
        subTopics.addSubTopic(subTopicID)
    }

    override fun removeSubTopic(subTopicID: String) {
        subTopics.removeSubTopic(subTopicID)
    }

    override fun getSubTopics(): List<String> {
        return subTopics.getSubTopics()
    }

    //OPINION
    override fun dislike(email: String) {
        opinions.dislike(email)
    }

    override fun dislikeToLike(email: String) {
        opinions.dislikeToLike(email)
    }

    override fun like(email: String) {
        opinions.like(email)
    }

    override fun likeToDislike(email: String) {
        opinions.likeToDislike(email)
    }

    override fun removeDislike(email: String) {
        opinions.removeDislike(email)
    }

    override fun removeLike(email: String) {
        opinions.removeLike(email)
    }

    override fun getDislikes(): List<String> {
        return opinions.getDislikes()
    }

    override fun getLikes(): List<String> {
        return opinions.getLikes()
    }

}