package br.meetingplace.server.subjects.services.owner.group

import br.meetingplace.server.subjects.services.owner.OwnerType

data class GroupOwnerData(val groupOwnerID: String, val groupCreatorID: String, val type: OwnerType)