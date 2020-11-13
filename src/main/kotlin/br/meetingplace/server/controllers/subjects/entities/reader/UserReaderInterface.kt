package br.meetingplace.server.controllers.subjects.entities.reader

import br.meetingplace.server.controllers.dependencies.newRW.topic.main.TopicRWInterface
import br.meetingplace.server.controllers.dependencies.newRW.user.UserRWInterface
import br.meetingplace.server.dto.Login
import br.meetingplace.server.subjects.entities.User
import br.meetingplace.server.subjects.services.topic.SimplifiedTopic
import br.meetingplace.server.subjects.services.topic.Topic

interface UserReaderInterface {
    fun getMyUser(data: Login, rwUser: UserRWInterface): User
    fun getMyThreads(data: Login, rwUser: UserRWInterface, rwTopic: TopicRWInterface): MutableList<Topic>
    fun getMyTimeline(data: Login, rwUser: UserRWInterface, rwTopic: TopicRWInterface): MutableList<Topic>
}