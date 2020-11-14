package br.meetingplace.server.controllers.subjects.entities.reader

import br.meetingplace.server.controllers.readwrite.topic.TopicRWInterface
import br.meetingplace.server.controllers.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.Login
import br.meetingplace.server.subjects.services.topic.Topic

class UserReader private constructor() : UserReaderInterface {

    companion object {
        private val Class = UserReader()
        fun getClass() = Class
    }

    override fun getMyTopics(data: Login, rwUser: UserRWInterface, rwTopic: TopicRWInterface): MutableList<Topic> {
        val user = rwUser.read(data.email)
        val myThreads = mutableListOf<Topic>()
        lateinit var myTopicIds: List<String>

        if (user != null) {
            myTopicIds = user.getMyTopics()

            for (element in myTopicIds) {
                val topic = rwTopic.read(element, null)
                if (topic != null)
                    myThreads.add(topic)
            }
            return myThreads
        }
        return myThreads
    }//READ

    override fun getMyTimeline(data: Login, rwUser: UserRWInterface, rwTopic: TopicRWInterface): MutableList<Topic> { //NEEDS WORK HERE
        val user = rwUser.read(data.email)
        val myTimeline = mutableListOf<Topic>()

        if (user != null) {
            val followingIds = user.getFollowing()

            for (a in followingIds.indices) {
                val following = rwUser.read(followingIds[a])
                if (following != null) {
                    val followingThreads = following.getMyTopics()
                    for (element in followingThreads) {
                        val topic = rwTopic.read(element, null)
                        if (topic != null)
                            myTimeline.add(topic)
                    }
                }
            }
//
//            val communities = user.getCommunitiesIFollow()
//            for (i in communities.indices) {
//                val communities = rw.readCommunity(communities[i])
//                if (verify.verifyCommunity(community)) {
//                    val threads = community.getIdThreads()
//                    for (element in threads) {
//                        val thread = rw.readTopic(element.id, element.creator)
//                        if (verify.verifyTopic(thread))
//                            myTimeline.add(thread)
//                    }
//                }
//            }
            return myTimeline
        }
        return myTimeline
    }//READ
}