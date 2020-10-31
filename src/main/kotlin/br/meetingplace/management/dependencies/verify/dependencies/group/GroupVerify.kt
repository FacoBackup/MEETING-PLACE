package br.meetingplace.management.dependencies.verify.dependencies.group

import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.services.group.Group

class GroupVerify private constructor(): GroupVerifyInterface{
    companion object{
        private val Class= GroupVerify()
        fun getClass ()= Class
    }

    override fun verifyGroup(group: Group): Boolean {
        return group.getGroupId().isNotBlank()
    }
}