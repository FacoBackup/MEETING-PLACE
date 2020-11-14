package br.meetingplace.server.controllers.readwrite.topic

import br.meetingplace.server.subjects.services.topic.Topic

interface TopicRWInterface {
    fun read(id: String, mainTopic: String?): Topic?
    fun write(data: Topic)
    fun delete(data: Topic)
}