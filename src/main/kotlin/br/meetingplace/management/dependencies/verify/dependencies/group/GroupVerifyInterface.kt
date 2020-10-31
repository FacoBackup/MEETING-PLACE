package br.meetingplace.management.dependencies.verify.dependencies.group

import br.meetingplace.services.group.Group

interface GroupVerifyInterface {
    fun verifyGroup(group: Group): Boolean
}