package br.meetingplace.server.controllers.dependencies.newRW.topic.sub

import br.meetingplace.server.subjects.services.topic.Topic

interface SubTopicRWInterface {
    fun read(id: String, mainTopic: String): Topic
    fun write(data: Topic)
    fun delete(data: Topic)
}