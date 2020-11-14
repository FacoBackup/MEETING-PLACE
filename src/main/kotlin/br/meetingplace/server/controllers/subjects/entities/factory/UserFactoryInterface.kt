package br.meetingplace.server.controllers.subjects.entities.factory

import br.meetingplace.server.controllers.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.user.UserCreationData

interface UserFactoryInterface {
    fun create(newUser: UserCreationData, rwUser: UserRWInterface): Boolean
}