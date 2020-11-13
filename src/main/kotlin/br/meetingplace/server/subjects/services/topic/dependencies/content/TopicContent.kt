package br.meetingplace.server.subjects.services.topic.dependencies.content

class TopicContent private constructor() : TopicContentInterface {
    companion object {
        private val Class = TopicContent()
        fun getClass() = Class
    }

    private var header: String? = null
    private var body: String? = null
    private var footer: String? = null

    override fun addContent(header: String, body: String, footer: String) {
        this.header = header
        this.body = body
        this.footer = footer
    }

    override fun editHeader(header: String) {
        this.header = header
    }

    override fun editBody(body: String) {
        this.body = body
    }
}