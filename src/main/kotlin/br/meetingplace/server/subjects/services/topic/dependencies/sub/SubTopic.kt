package br.meetingplace.server.subjects.services.topic.dependencies.sub

class SubTopic private constructor() : SubTopicInterface {
    companion object {
        private val Class = SubTopic()
        fun getClass() = Class
    }

    private var subTopicIDs = mutableListOf<String>()

    override fun getSubTopics() = subTopicIDs
    override fun addSubTopic(subTopicID: String) {
        subTopicIDs.add(subTopicID)

    }

    override fun removeSubTopic(subTopicID: String) {
        subTopicIDs.remove(subTopicID)
    }
}