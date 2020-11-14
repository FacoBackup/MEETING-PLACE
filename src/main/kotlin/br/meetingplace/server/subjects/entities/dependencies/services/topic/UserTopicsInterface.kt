package br.meetingplace.server.subjects.entities.dependencies.services.topic

interface UserTopicsInterface {
    fun updateMyTopics(topic: String, add: Boolean)
    fun getMyTopics(): List<String>
}