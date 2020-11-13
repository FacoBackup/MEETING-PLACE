package br.meetingplace.server.controllers.subjects.services.topic.like

import br.meetingplace.server.dto.topics.TopicOperationsData

interface LikeInterface {
    fun like(data: TopicOperationsData)
}