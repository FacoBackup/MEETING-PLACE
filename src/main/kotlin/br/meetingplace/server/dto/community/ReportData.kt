package br.meetingplace.server.dto.community

import br.meetingplace.server.dto.Identifier
import br.meetingplace.server.dto.Login

data class ReportData(val reason: String?, val identifier: Identifier, val login: Login)
//ID == id topic
//communityID is a requirement
