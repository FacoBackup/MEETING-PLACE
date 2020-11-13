package br.meetingplace.server.subjects.services.topic

import br.meetingplace.server.subjects.services.owner.topic.TopicOwnerData
import br.meetingplace.server.subjects.services.topic.dependencies.Controller

class Topic(private val owner: TopicOwnerData, private val creator: String, private var id: String, private var mainTopic: String?) : Controller() {
    fun getID() = id
    fun getCreator() = creator
    fun getOwner() = owner
    fun getMainTopic() = mainTopic
}