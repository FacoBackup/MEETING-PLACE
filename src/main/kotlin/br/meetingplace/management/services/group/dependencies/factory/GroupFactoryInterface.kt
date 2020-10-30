package br.meetingplace.management.services.group.dependencies.factory

import br.meetingplace.data.group.GroupData
import br.meetingplace.data.group.GroupOperationsData

interface GroupFactoryInterface {
    fun create(data: GroupData)
    fun delete(data: GroupOperationsData)
}