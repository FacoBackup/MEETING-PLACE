package br.meetingplace.management.user

import br.meetingplace.data.user.LoginByEmail
import br.meetingplace.data.user.UserData
import br.meetingplace.entitie.User
import br.meetingplace.management.Login
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.management.user.dependencies.Profile


class UserFactory: ReadWriteLoggedUser, ReadWriteUser, Profile() {
    fun createUser(newUser: UserData){
        val logged = readLoggedUser().email
        val user =  br.meetingplace.entitie.User(newUser.realName, newUser.age, newUser.email.toLowerCase(), newUser.password)

        if(logged == "" && user.getAge() > 0){
            writeUser(user,user.getEmail())
            Login.getLoginSystem().login(LoginByEmail(user.getEmail(), user.getPassword()))
        }
    }//CREATE

    fun getMyUser(): User{
        val logged = readLoggedUser().email
        return readUser(logged)
    }//READ

    fun deleteUser(){

        val logged = readLoggedUser().email
        val user = readUser(logged)
        if(verifyUser(user)){

            val followers = user.social.getFollowers()
            val following = user.social.getFollowing()

            for(i in 0 until followers.size){
                val userFollower = readUser(followers[i])
                if(userFollower.getAge() != -1) {
                    userFollower.social.updateFollowing(logged, true)
                    writeUser(userFollower, userFollower.getEmail())
                }
            }

            for(i in 0 until following.size){
                val userFollowing = readUser(following[i])
                if(userFollowing.getAge() != -1){
                    userFollowing.social.updateFollowers(logged,true)
                    writeUser(userFollowing,userFollowing.getEmail())
                }
            }
            /*
                for(i in 0 until groupList.size){
                    member = UserMember(management,groupList[i].getEmail())
                    removeMember(member) // should use an override here
                }

             */
            //deleteAllThreadsFromUserId()
            Login.getLoginSystem().logout()
        }
    }//DELETE
}