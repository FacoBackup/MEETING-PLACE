package br.meetingplace.server.subjects.services.topic.dependencies.content

interface TopicContentInterface {
    fun addContent(header: String, body: String, footer: String)
    fun editHeader(header: String)
    fun editBody(body: String)
}