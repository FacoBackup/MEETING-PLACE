package br.meetingplace.server.subjects.services.topic.dependencies.opinions


class TopicOpinion private constructor() : TopicOpinionInterface {
    companion object {
        private val Class = TopicOpinion()
        fun getClass() = Class
    }

    private var likes = mutableListOf<String>()
    private var dislikes = mutableListOf<String>()

    override fun getLikes() = likes
    override fun getDislikes() = dislikes
    override fun like(email: String) {
        likes.add(email)
    }

    override fun removeLike(email: String) {
        likes.remove(email)
    }

    override fun dislike(email: String) {
        dislikes.add(email)
    }

    override fun removeDislike(email: String) {
        dislikes.remove(email)
    }

    override fun likeToDislike(email: String) {
        if (email in likes) {
            likes.remove(email)
            dislikes.add(email)
        }
    }

    override fun dislikeToLike(email: String) {
        if (email in dislikes) {
            dislikes.remove(email)
            likes.add(email)
        }
    }
}