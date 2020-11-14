package br.meetingplace.server.subjects.services.owner.chat

import br.meetingplace.server.subjects.services.owner.OwnerType

data class ChatOwnerData(val firstOwnerID: String, val secondOwnerID: String, val mainOwnerType: OwnerType, val subOwnerType: OwnerType)