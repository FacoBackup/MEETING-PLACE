package br.meetingplace.server.dto.community

import br.meetingplace.server.dto.Identifier
import br.meetingplace.server.dto.Login

data class ApprovalData(val identifier: Identifier, val login: Login)