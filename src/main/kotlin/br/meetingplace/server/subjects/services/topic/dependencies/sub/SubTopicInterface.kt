package br.meetingplace.server.subjects.services.topic.dependencies.sub

interface SubTopicInterface {
    fun addSubTopic(subTopicID: String)
    fun removeSubTopic(subTopicID: String)
    fun getSubTopics(): List<String>
}