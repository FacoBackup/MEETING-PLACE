package br.meetingplace.management.services.group.core

import br.meetingplace.data.group.GroupData
import br.meetingplace.data.group.GroupOperationsData
import br.meetingplace.data.group.MemberInput
import br.meetingplace.management.services.group.dependencies.factory.GroupFactory
import br.meetingplace.management.services.group.dependencies.factory.GroupFactoryInterface
import br.meetingplace.management.services.group.dependencies.management.GroupManagement
import br.meetingplace.management.services.group.dependencies.management.GroupManagementInterface
import br.meetingplace.management.services.group.dependencies.reader.GroupReader
import br.meetingplace.management.services.group.dependencies.reader.GroupReaderInterface
import br.meetingplace.services.group.Group


class GroupCore private constructor(): GroupFactoryInterface, GroupManagementInterface, GroupReaderInterface{

    companion object{
        private val Class = GroupCore()
        fun getClass () = Class
    }

    private val factory = GroupFactory.getClass()
    private val management = GroupManagement.getClass()
    private val reader = GroupReader.getClass()

    //FACTORY
    override fun create(data: GroupData) {
        factory.create(data)
    }
    override fun delete(data: GroupOperationsData) {
        factory.delete(data)
    }

    //MANAGEMENT
    override fun addMember(data: MemberInput) {
        management.addMember(data)
    }
    override fun removeMember(data: MemberInput) {
        management.removeMember(data)
    }

    //READER
    override fun readMemberIn(): MutableList<Group> {
        return reader.readMemberIn()
    }
    override fun readMyGroups(): MutableList<Group> {
        return reader.readMyGroups()
    }

}