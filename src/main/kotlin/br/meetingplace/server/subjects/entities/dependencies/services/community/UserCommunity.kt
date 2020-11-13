package br.meetingplace.server.subjects.entities.dependencies.services.community

class UserCommunity private constructor() : UserCommunityInterface {
    companion object {
        private val Class = UserCommunity()
        fun getClass() = Class
    }

    private var moderatorIn = mutableListOf<String>()
    private var communitiesIFollow = mutableListOf<String>()

    override fun updateModeratorIn(id: String, leave: Boolean) {
        when (leave) {
            true -> {
                if (id in moderatorIn)
                    moderatorIn.remove(id)
            }
            false -> {
                if (id !in moderatorIn)
                    moderatorIn.add(id)
            }
        }
    }

    override fun updateCommunitiesIFollow(id: String, unfollow: Boolean) {
        when (unfollow) {
            true -> {
                if (id in communitiesIFollow)
                    communitiesIFollow.remove(id)
            }
            false -> {
                if (id !in communitiesIFollow)
                    communitiesIFollow.add(id)
            }
        }
    }

    override fun getModeratorIn() = moderatorIn
    override fun getCommunitiesIFollow() = communitiesIFollow

}