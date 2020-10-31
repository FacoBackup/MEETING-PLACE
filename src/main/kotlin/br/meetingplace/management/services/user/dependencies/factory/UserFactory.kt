package br.meetingplace.management.services.user.dependencies.factory

import br.meetingplace.data.user.LoginByEmail
import br.meetingplace.data.user.UserData
import br.meetingplace.management.dependencies.verify.dependencies.Verify
import br.meetingplace.management.dependencies.ReadWriteController
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.services.entitie.User
import br.meetingplace.management.services.Login
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.readwrite.dependencies.thread.ReadWriteThread
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteUser
import br.meetingplace.services.entitie.profiles.followdata.FollowData


class UserFactory private constructor(): UserInterface, ReadWriteLoggedUser, ReadWriteUser, Verify, ReadWriteThread {
    private val iDs = IDsController.getClass()
    companion object{
        private val Class = UserFactory()
        fun getClass()= Class
    }

    override fun create(newUser: UserData){
        val logged = readLoggedUser().email
        val user = User(newUser.userName, newUser.age, newUser.email.toLowerCase(), newUser.password)

        if(logged == "" && user.getAge() > 0){
            writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
            Login.getLoginSystem().login(LoginByEmail(user.getEmail(), user.getPassword()))
        }
    }//CREATE
    
    override fun delete(){
        val logged = readLoggedUser().email
        val user = readUser(logged)
        lateinit var followers: List<FollowData>
        lateinit var following: List<FollowData>
        lateinit var userExternal: User

        if(verifyUser(user)){

            followers = user.getFollowers()
            following = user.getFollowing()

            for(index in followers.indices){
                userExternal = readUser(followers[index].email)
                if(userExternal.getAge() != -1) {
                    userExternal.updateFollowing(FollowData(user.getUserName(),logged), true)
                    writeUserToFile(userExternal,iDs.attachNameToEmail(userExternal.getUserName(),userExternal.getEmail()))
                }
            }

            for(index in following.indices){
                userExternal = readUser(following[index].email)
                if(userExternal.getAge() != -1){
                    userExternal.updateFollowers(FollowData(user.getUserName(),logged),true)
                    writeUserToFile(userExternal,iDs.attachNameToEmail(userExternal.getUserName(),userExternal.getEmail()))
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
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)

        if(verifyLoggedUser(user)){
            val myThreads = user.getMyThreads()
            for(index in myThreads){
                val thread = readThread(index)
                if(verifyThread(thread))
                    ReadWriteController.getDeleteFileOperator().deleteThread(thread)
            }
        }
    } //DELETE
}