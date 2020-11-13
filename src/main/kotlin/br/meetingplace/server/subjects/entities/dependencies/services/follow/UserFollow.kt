package br.meetingplace.server.subjects.entities.dependencies.services.follow

class UserFollow private constructor() : UserFollowInterface {
    companion object {
        private val Class = UserFollow()
        fun getClass() = Class
    }

    private var followers = mutableListOf<String>()
    private var following = mutableListOf<String>()

    override fun getFollowing() = following
    override fun getFollowers() = followers
    override fun updateFollowers(data: String, remove: Boolean) {
        when (remove) {
            true -> {
                if (data in followers)
                    followers.remove(data)
            }
            false -> {
                if (data !in followers)
                    followers.add(data)
            }
        }
    }

    override fun updateFollowing(data: String, remove: Boolean) {
        when (remove) {
            true -> {
                if (data in following)
                    following.remove(data)
            }
            false -> {
                if (data !in following)
                    following.add(data)
            }
        }
    }
}