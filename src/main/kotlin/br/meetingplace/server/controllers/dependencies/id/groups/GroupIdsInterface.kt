package br.meetingplace.server.controllers.dependencies.id.groups

import br.meetingplace.server.subjects.entities.User

interface GroupIdsInterface {

    fun getGroupId(groupName: String, creator: String?): String
    fun getGroupSimpleId(id: String): String
    fun simpleToStandardIdGroup(id: String, user: User): String
    fun standardToSimpleIdGroup(id: String): String
}