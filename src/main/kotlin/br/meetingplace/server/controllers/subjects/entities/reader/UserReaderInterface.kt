package br.meetingplace.server.controllers.subjects.entities.reader

import br.meetingplace.server.controllers.dependencies.readwrite.topic.main.TopicRWInterface
import br.meetingplace.server.controllers.dependencies.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.Login
import br.meetingplace.server.subjects.services.topic.Topic

interface UserReaderInterface {
    fun getMyThreads(data: Login, rwUser: UserRWInterface, rwTopic: TopicRWInterface): MutableList<Topic>
    fun getMyTimeline(data: Login, rwUser: UserRWInterface, rwTopic: TopicRWInterface): MutableList<Topic>
}