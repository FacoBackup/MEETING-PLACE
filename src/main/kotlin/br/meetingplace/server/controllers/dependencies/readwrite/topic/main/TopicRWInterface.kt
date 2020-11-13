package br.meetingplace.server.controllers.dependencies.readwrite.topic.main

import br.meetingplace.server.subjects.services.topic.Topic

interface TopicRWInterface {
    fun read(id: String): Topic?
    fun write(data: Topic)
    fun delete(data: Topic)
}