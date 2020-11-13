package br.meetingplace.server.controllers.subjects.services.group.members

import br.meetingplace.server.dto.MemberOperator

interface GroupMembersInterface {
    fun addMember(data: MemberOperator)
    fun removeMember(data: MemberOperator)
    fun changeMemberRole(data: MemberOperator)
}