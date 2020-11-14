package br.meetingplace.server.controllers.subjects.services.search.group

import br.meetingplace.server.controllers.readwrite.group.GroupRWInterface
import br.meetingplace.server.controllers.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.SimpleOperator
import br.meetingplace.server.subjects.services.groups.Group

class GroupSearch private constructor() {
    companion object {
        private val Class = GroupSearch()
        fun getClass() = Class
    }

    fun seeGroup(data: SimpleOperator, rwUser: UserRWInterface, rwGroup: GroupRWInterface): Group? {
        val user = rwUser.read(data.login.email)

        return if (user != null && data.identifier.owner != null) {
            if (data.identifier.ID in user.getMyGroups()) {
                return rwGroup.read(data.identifier.ID)
            } else if (data.identifier.ID in user.getMemberIn()) {
                return rwGroup.read(data.identifier.ID)
            }
            null
        } else null
    }
}