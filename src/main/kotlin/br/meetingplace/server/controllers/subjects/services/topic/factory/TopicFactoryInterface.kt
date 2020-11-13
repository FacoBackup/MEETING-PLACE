package br.meetingplace.server.controllers.subjects.services.topic.factory

import br.meetingplace.server.dto.topics.TopicData

interface TopicFactoryInterface {
    fun create(data: TopicData)
}