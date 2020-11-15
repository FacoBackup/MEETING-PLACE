package br.meetingplace.server.subjects.services.owner.chat

import br.meetingplace.server.subjects.services.owner.OwnerType

data class ChatOwnerData(val mainOwnerID: String, val subOwnerID: String, val mainOwnerType: OwnerType, val subOwnerType: OwnerType)