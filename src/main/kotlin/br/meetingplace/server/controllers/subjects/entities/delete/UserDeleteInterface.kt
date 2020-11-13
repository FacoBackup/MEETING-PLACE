package br.meetingplace.server.controllers.subjects.entities.delete

import br.meetingplace.server.controllers.dependencies.readwrite.topic.main.TopicRWInterface
import br.meetingplace.server.controllers.dependencies.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.Login

interface UserDeleteInterface {
    fun delete(data: Login, rwUser: UserRWInterface,  rwTopic: TopicRWInterface)
}