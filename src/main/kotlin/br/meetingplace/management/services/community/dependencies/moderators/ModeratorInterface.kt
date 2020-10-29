package br.meetingplace.management.services.community.dependencies.moderators

import br.meetingplace.data.community.ApprovalData

interface ModeratorInterface {
    fun approveThread(data: ApprovalData)
    fun approveGroup(data: ApprovalData)
}