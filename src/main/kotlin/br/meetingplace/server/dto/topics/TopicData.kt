package br.meetingplace.server.dto.topics

import br.meetingplace.server.dto.Login

data class TopicData(
        val header: String,
        val body: String,
        val identifier: TopicIdentifier?,
        val communityID: String?,
        val login: Login
)