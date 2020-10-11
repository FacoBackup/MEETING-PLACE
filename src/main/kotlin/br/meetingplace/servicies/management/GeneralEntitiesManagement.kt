package br.meetingplace.servicies.management

import br.meetingplace.entities.grupos.Group
import br.meetingplace.data.UserMember
import br.meetingplace.data.Follower
import br.meetingplace.data.Login
import br.meetingplace.data.Operations
import br.meetingplace.entities.user.User
import br.meetingplace.entities.user.profiles.ProfessionalProfile
import br.meetingplace.entities.user.profiles.SocialProfile
import br.meetingplace.servicies.conversationThread.MainThread
import kotlin.random.Random

open class GeneralEntitiesManagement{

        protected val groupList = mutableListOf<Group>()
        protected val userList = mutableListOf<User>()
        private var logged = -1
        protected var cachedPass = ""
        //GETTERS
        fun getGroups() = groupList
        fun getUsers() = userList
        fun getLoggedUser() = logged

        fun getSocialNameById(id: Int): String{
            if(verifyUser(id)){
                val index = getUserIndex(id)
                return userList[index].social.userName
            }
            return ""
        }
        //GETTERS

    //STARTUP NECESSITIES
        fun createUser(user: User){
            if(user.getId() == -1 && logged == -1){
                user.startUser(generateUserId())
                userList.add(user)
            }
        }

        fun createSocialProfile(user: SocialProfile){

            if(getLoggedUser() != -1){

                val indexUser = getUserIndex(getLoggedUser())
                userList[indexUser].socialProfile(user)
            }
        }

        fun createProfessionalProfile(user: ProfessionalProfile){

            if(getLoggedUser() != -1)
                createProfessionalProfile(user)
        }
        fun deleteUser(operation: Operations){

            val management = EntitiesManagement() // should use an override here
            if(logged != -1 && operation.id == logged && operation.pass == cachedPass){

                val indexUser = getUserIndex(operation.id)
                var member: UserMember

                    for(i in 0 until userList.size)
                        userList[i].social.followers.remove(operation.id)

                    for(i in 0 until groupList.size){
                        member = UserMember(operation.id,groupList[i].getId())
                        management.removeMember(member) // should use an override here
                    }

                    userList.remove(userList[indexUser])
                    logoff()
            }
        }

        fun createGroup(group: Group){

            group.updateCreator(logged)
            val indexCreator = getUserIndex(logged)

            if(group.getId() == -1 && verifyGroupName(group.getNameGroup())){
                group.startGroup(generateGroupId())
                userList[indexCreator].social.groups.add(group.getId())
                if(logged != -1)
                    groupList.add(group)
            }
        }

        protected fun deleteGroup(member: UserMember){

            val indexGroup = getGroupIndex(member.group)

            if(indexGroup != -1 && groupList[indexGroup].getCreator() == member.id && member.id == logged){ //ONLY THE CREATOR OF THE GROUP CAN DELETE IT
                // percorre a lista de usuarios e remove todos que fazem parte do grupo
                for(i in 0 until userList.size)
                    userList[i].social.groups.remove(member.group) // removing the group id from the users profile

                groupList.remove(groupList[indexGroup])
            }
        }

        //AUTHENTICATION SYSTEM
        fun login(log: Login){

            val indexUser = getUserIndex(log.user)
            if(verifyUser(log.user) && userList[indexUser].getPass() == log.pass && logged == -1){
                logged = log.user
                cachedPass = log.pass
            }


        }
        fun logoff(){

            if(logged != -1){
                logged = -1
                cachedPass = ""
            }
        }
        //AUTHENTICATION SYSTEM

    // STARTUP NECESSITIES

    //GENERATORS
        private fun generateUserId(): Int{
            var id = Random.nextInt(1, 20000)

            for (i in userList){
                while(id == i.getId())
                    id = Random.nextInt(1, 20000)
            }
            return id
        }

        fun generateThreadId(ThreadList: List<MainThread>): Int{
            var id = Random.nextInt(1, 20000)

            for (element in ThreadList){
                while(id == element.getId())
                    id = Random.nextInt(1, 20000)
            }
            return id
        }

        private fun generateGroupId(): Int {
            var id = Random.nextInt(1, 20000)

            for (i in groupList) {
                if (i.getId() == id) {
                    while (id == i.getId()) {
                        id = Random.nextInt(1, 20000)
                    }
                }
            }
            return id
        }
    //GENERATORS

    //VERIFIERS
        private fun verifyUserName(name: String): Boolean {
            for(i in 0 until userList.size){
                if(userList[i].social.userName == name)
                    return false
            }
            return true
        }

        fun verifyUser(id: Int): Boolean {

            for(i in 0 until userList.size){
                if(userList[i].getId() == id)
                    return true
            }
            return false
        }

        private fun verifyGroupName(name: String): Boolean {
            for(i in 0 until groupList.size){
                if(groupList[i].getNameGroup() == name)
                    return false
            }
            return true
        }

        private fun verifyGroup(id: Int): Boolean {

            for(i in 0 until groupList.size){
                if(groupList[i].getId() == id)
                    return true
            }
            return false
        }

        protected fun verifyFollower(data: Follower): Int{
            val indexExternal = getUserIndex(data.external)

            for (i in 0 until userList[indexExternal].social.followers.size){
                if(userList[indexExternal].social.followers[i] == logged){
                    return 1
                }
            }
            return 0
        }
    //VERIFIERS

    //INDEX FINDERS
        protected fun getThreadIndex( threadId: Int): Int {

            if(getLoggedUser() != -1){
                val indexUser = getUserIndex(getLoggedUser())
                val thread = userList[indexUser].social.getThreads()

                for (i in 0 until thread.size){
                    if(thread[i].getId() == threadId)
                        return i
                }
                return -1
            }
        return -1
        }
        protected fun getGroupIndex(id: Int): Int {
            if(verifyGroup(id)){
                for(i in 0 until groupList.size) {
                    if (groupList[i].getId() == id)
                        return i
                }
                return -1
            }
            else return -1
        }

        protected fun getUserIndex(id: Int): Int {
            if(verifyUser(id)) {
                for (i in 0 until userList.size) {
                    if (userList[i].getId() == id)
                        return i
                }
                return -1
            }
            else return -1
        }

        protected fun getMemberIndex(id: Int, idGroup: Int): Int {
            val indexGroup = getGroupIndex(idGroup)

            if(indexGroup != -1 && groupList[indexGroup].members.size > 0){
                for(i in 0 until groupList[indexGroup].members.size) {
                    if (groupList[indexGroup].members[i].user == id)
                        return i
                }
                return -1
            }
            return -1
        }
    //INDEX FINDERS
}