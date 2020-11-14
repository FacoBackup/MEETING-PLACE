package br.meetingplace.server.controllers.subjects.entities.reader

import br.meetingplace.server.controllers.readwrite.topic.TopicRWInterface
import br.meetingplace.server.controllers.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.Login
import br.meetingplace.server.subjects.services.topic.Topic

interface UserReaderInterface {
    fun getMyTopics(data: Login, rwUser: UserRWInterface, rwTopic: TopicRWInterface): MutableList<Topic>
    fun getMyTimeline(data: Login, rwUser: UserRWInterface, rwTopic: TopicRWInterface): MutableList<Topic>
}