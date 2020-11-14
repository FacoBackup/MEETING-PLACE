package br.meetingplace.server.controllers.subjects.entities.factory

import br.meetingplace.server.controllers.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.user.UserCreationData
import br.meetingplace.server.subjects.entities.User


class UserFactory private constructor() : UserFactoryInterface {

    companion object {
        private val Class = UserFactory()
        fun getClass() = Class
    }

    override fun create(newUser: UserCreationData, rwUser: UserRWInterface): Boolean {
        val user = User(newUser.userName, newUser.age, newUser.email.toLowerCase(), newUser.password)

        return if (newUser.age >= 16) {
            val existingUser = rwUser.read(user.getEmail())
            println (existingUser)
            return if (existingUser == null) {
                rwUser.write(user)
                true
            } else false
        } else false
    }
}