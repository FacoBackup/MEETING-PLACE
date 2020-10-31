package br.meetingplace.management.dependencies.verify.dependencies.user

import br.meetingplace.services.entitie.User

interface UserVerifyInterface {
    fun verifyUser(user: User): Boolean
    fun verifyFollower(external: User, user: User): Boolean
}