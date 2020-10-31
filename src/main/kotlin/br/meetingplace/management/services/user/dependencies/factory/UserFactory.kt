package br.meetingplace.management.services.user.dependencies.factory

import br.meetingplace.data.user.LoginByEmail
import br.meetingplace.data.user.UserData
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.services.entitie.User
import br.meetingplace.management.services.Login
import br.meetingplace.services.entitie.profiles.followdata.FollowData


class UserFactory private constructor(): UserInterface{
    private val iDs = IDsController.getClass()
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()

    companion object{
        private val Class = UserFactory()
        fun getClass()= Class
    }

    override fun create(newUser: UserData){
        val logged = rw.readLoggedUser().email
        val user = User(newUser.userName, newUser.age, newUser.email.toLowerCase(), newUser.password)

        if(logged == "" && user.getAge() > 0){
            rw.writeUser(user,user.getEmail())
            Login.getLoginSystem().login(LoginByEmail(user.getUserName(), user.getEmail(), user.getPassword()))
        }
    }//CREATE
    
    override fun delete(){
        val logged = rw.readLoggedUser().email
        val user = rw.readUserFromFile(logged)

        lateinit var followers: List<FollowData>
        lateinit var following: List<FollowData>
        lateinit var userExternal: User

        if(verify.verifyUser(user)){

            followers = user.getFollowers()
            following = user.getFollowing()

            for(index in followers.indices){
                userExternal = rw.readUserFromFile(followers[index].email)
                if(userExternal.getAge() != -1) {
                    userExternal.updateFollowing(FollowData(user.getUserName(),logged), true)
                    rw.writeUser(userExternal,userExternal.getEmail())
                }
            }

            for(index in following.indices){
                userExternal = rw.readUserFromFile(following[index].email)
                if(userExternal.getAge() != -1){
                    userExternal.updateFollowers(FollowData(user.getUserName(),logged),true)
                    rw.writeUser(userExternal,userExternal.getEmail())
                }
            }
            /*
                for(i in 0 until groupList.size){
                    member = UserMember(management,groupList[i].getEmail())
                    removeMember(member) // should use an override here
                }
             */
            deleteAllThreadsFromUserId()
            Login.getLoginSystem().logout()
        }
    }//DELETE

    private fun deleteAllThreadsFromUserId(){
        val loggedUser = rw.readLoggedUser().email
        val user =rw.readUserFromFile(loggedUser)

        if(verify.verifyUser(user)){
            val myThreads = user.getMyThreads()
            for(index in myThreads){
                val thread = rw.readThread(index)
                if(verify.verifyThread(thread))
                   rw.deleteThread(thread)
            }
        }
    } //DELETE
}