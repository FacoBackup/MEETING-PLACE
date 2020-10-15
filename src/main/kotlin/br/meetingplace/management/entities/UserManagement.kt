package br.meetingplace.management.entities

import br.meetingplace.data.PasswordOperations
import br.meetingplace.data.entities.group.UserMember
import br.meetingplace.data.startup.UserData
import br.meetingplace.entities.user.User

class UserManagement: ProfileManagement() {

    fun createUser(newUser: UserData){

        val user = User(newUser.realName, newUser.age, newUser.email, newUser.password)

        if(user.getId() == -1 && getLoggedUser() == -1 && user.getEmail() !in emailList && user.getEmail() != ""){
            user.startUser(generateUserId())
            userList.add(user)
            emailList.add(user.getEmail())
        }
    }

    fun deleteUser(operation: PasswordOperations){

        if(getLoggedUser() != -1 && operation.pass == cachedPass){

            val indexUser = getUserIndex(getLoggedUser())
            var member: UserMember

            for(i in 0 until userList[indexUser].social.followers.size){
                val indexFollower = getUserIndex(userList[indexUser].social.followers[i])
                if(indexFollower != -1)
                    userList[indexFollower].social.following.remove(getLoggedUser())
            }

            for(i in 0 until userList[indexUser].social.following.size){
                val indexFollowing = getUserIndex(userList[indexUser].social.following[i])
                if(indexFollowing != -1)
                    userList[indexFollowing].social.followers.remove(getLoggedUser())
            }

            for(i in 0 until groupList.size){
                member = UserMember(getLoggedUser(),groupList[i].getId())
                removeMember(member) // should use an override here
            }

            deleteAllThreadsFromUserId()

            emailList.remove(userList[indexUser].getEmail())

            if(userList[indexUser].social.getUserName() != "")
                nameList.remove(userList[indexUser].social.getUserName())

            userList.remove(userList[indexUser])

            logoff()
        }
    }
}