package br.meetingplace.server.subjects.services.owner.topic

import br.meetingplace.server.subjects.services.owner.OwnerType

data class TopicOwnerData(val mainTopicOwnerID: String, val mainTopicCreatorID: String, val type: OwnerType)