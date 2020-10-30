package br.meetingplace.management.services.group.dependencies.management

import br.meetingplace.data.group.MemberInput
import br.meetingplace.services.group.Group

interface GroupManagementInterface {
    fun addMember(data: MemberInput)
    fun removeMember(data: MemberInput)
}