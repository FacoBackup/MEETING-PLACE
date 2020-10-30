package br.meetingplace.management.services.group.dependencies.reader

import br.meetingplace.services.group.Group

interface GroupReaderInterface {
    fun readMyGroups(): MutableList<Group>
    fun readMemberIn(): MutableList<Group>
}