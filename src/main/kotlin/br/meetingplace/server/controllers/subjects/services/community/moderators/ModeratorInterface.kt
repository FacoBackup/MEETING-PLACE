package br.meetingplace.server.controllers.subjects.services.community.moderators

import br.meetingplace.server.dto.MemberOperator
import br.meetingplace.server.dto.community.ApprovalData

interface ModeratorInterface {
    fun approveTopic(data: ApprovalData)
    fun approveGroup(data: ApprovalData)
    fun stepDown(data: MemberOperator)
}