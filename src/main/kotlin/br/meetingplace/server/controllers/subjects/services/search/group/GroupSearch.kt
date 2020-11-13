package br.meetingplace.server.controllers.subjects.services.search.group

import br.meetingplace.server.controllers.dependencies.id.controller.IDController
import br.meetingplace.server.controllers.dependencies.rw.controller.RWController
import br.meetingplace.server.controllers.dependencies.verify.controller.VerifyController
import br.meetingplace.server.dto.SimpleOperator
import br.meetingplace.server.subjects.services.groups.Group

class GroupSearch private constructor() : GroupSearchInterface {
    private val rw = RWController.getClass()
    private val verify = VerifyController.getClass()
    private val iDs = IDController.getClass()

    companion object {
        private val Class = GroupSearch()
        fun getClass() = Class
    }

    override fun seeGroup(data: SimpleOperator): Group? {
        val user = rw.readUser(data.login.email)

        return if (verify.verifyUser(user) && data.identifier.owner != null) {
            if (data.identifier.ID in user.getMyGroups()) {
                return rw.readGroup(data.identifier.ID, data.identifier.owner, false)
            } else if (data.identifier.ID in user.getMemberIn()) {
                return rw.readGroup(data.identifier.ID, data.identifier.owner, false)
            }
            null
        } else null
    }
}