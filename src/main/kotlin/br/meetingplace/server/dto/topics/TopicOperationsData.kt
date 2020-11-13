package br.meetingplace.server.dto.topics

import br.meetingplace.server.dto.Login

data class TopicOperationsData(val identifier: TopicIdentifier, val communityID: String?, val login: Login)