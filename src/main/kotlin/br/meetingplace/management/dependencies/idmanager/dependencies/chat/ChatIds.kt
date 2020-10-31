package br.meetingplace.management.dependencies.idmanager.dependencies.chat

class ChatIds private constructor(): ChatIdsInterface{
    companion object{
        private val Class = ChatIds()
        fun getClass() = Class
    }

    override fun getChatId(firstId: String, secondId: String): String {
        return firstId.removeSuffix("@") + secondId.removeSuffix("@")
    }
}