package br.meetingplace.server.subjects.services.topic

import br.meetingplace.server.subjects.services.owner.topic.TopicOwnerData

data class SimplifiedTopic(val ID: String, val owner: TopicOwnerData)
