package br.meetingplace.servicies.management

import br.meetingplace.data.Operations
import br.meetingplace.data.user.UserMember
import br.meetingplace.entities.user.User

class UserManagement: ProfileManagement() {
    fun createUser(user: User){
        if(user.getId() == -1 && getLoggedUser() == -1){
            user.startUser(generateUserId())
            userList.add(user)
        }
    }

    fun deleteUser(operation: Operations){


        if(getLoggedUser() != -1 && operation.id == getLoggedUser() && operation.pass == cachedPass){

            val indexUser = getUserIndex(operation.id)
            var member: UserMember

            for(i in 0 until userList.size)
                userList[i].social.followers.remove(operation.id)

            for(i in 0 until groupList.size){
                member = UserMember(operation.id,groupList[i].getId())
                removeMember(member) // should use an override here
            }

            userList.remove(userList[indexUser])
            logoff()
        }
    }
}