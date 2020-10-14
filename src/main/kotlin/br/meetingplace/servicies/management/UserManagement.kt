package br.meetingplace.servicies.management

import br.meetingplace.data.PasswordOperations
import br.meetingplace.data.user.UserMember
import br.meetingplace.entities.user.User

class UserManagement: ProfileManagement() {

    fun createUser(user: User){
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
            if(userList[indexUser].social.userName != "")
                nameList.remove(userList[indexUser].social.userName)
            userList.remove(userList[indexUser])
            logoff()
        }
    }
}