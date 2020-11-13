package br.meetingplace.server.controllers.subjects.services.topic.dislike

import br.meetingplace.server.dto.topics.TopicOperationsData

interface DislikeInterface {
    fun dislike(data: TopicOperationsData)
}