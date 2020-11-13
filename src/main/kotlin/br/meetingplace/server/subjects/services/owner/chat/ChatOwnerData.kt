package br.meetingplace.server.subjects.services.owner.chat

import br.meetingplace.server.subjects.services.owner.OwnerType

data class ChatOwnerData(val chatMainOwnerID: String, val chatSubOwnerID: String, val mainOwnerType: OwnerType, val subOwnerType: OwnerType)