package br.meetingplace.server.subjects.entities.dependencies.services.topic

import br.meetingplace.server.subjects.services.topic.SimplifiedTopic

interface UserTopicsInterface {
    fun updateMyTopics(topic: String, add: Boolean)
    fun getMyTopics(): List<String>
}