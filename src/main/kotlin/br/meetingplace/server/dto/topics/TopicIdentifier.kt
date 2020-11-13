package br.meetingplace.server.dto.topics

data class TopicIdentifier(val mainTopicID: String, val subTopicID: String?, val mainTopicOwner: String, val community: Boolean)
