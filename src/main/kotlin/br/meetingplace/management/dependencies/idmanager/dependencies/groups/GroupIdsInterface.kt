package br.meetingplace.management.dependencies.idmanager.dependencies.groups

import br.meetingplace.services.entitie.User

interface GroupIdsInterface {

    fun getGroupId(groupName: String, creator:String?): String
    fun getGroupSimpleId(id: String): String
    fun simpleToStandardIdGroup(id: String, user: User): String
    fun standardToSimpleIdGroup(id: String): String
}