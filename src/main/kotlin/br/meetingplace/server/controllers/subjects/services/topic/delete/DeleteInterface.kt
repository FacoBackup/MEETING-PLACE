package br.meetingplace.server.controllers.subjects.services.topic.delete

import br.meetingplace.server.dto.topics.TopicOperationsData

interface DeleteInterface {
    fun delete(data: TopicOperationsData)
}