package br.meetingplace.management

import br.meetingplace.entities.grupos.Group
import br.meetingplace.data.UserMember
import br.meetingplace.data.Follower
import br.meetingplace.data.Login
import br.meetingplace.entities.usuario.Profile
import br.meetingplace.servicies.Authentication
import kotlin.random.Random

open class GeneralEntitiesManagement{

        protected val groupList = mutableListOf<Group>()
        protected val userList = mutableListOf<Profile>()
        private var logged = -1
        private val auto = Authentication()

        //GETTERS
        fun getGroups() = groupList
        fun getUsers() = userList
        fun getUserLogged() = logged
        fun getUserLoggedAuto() = auto.getLoggedUser()
        //GETTERS

    //STARTUP NECESSITIES
        fun createUser(user: Profile){

            if(user.getId() == -1 && verifyUserName(user.userName)){
                user.updateId(generateIdUser())
                userList.add(user)
            }
        }

        protected fun deleteUser(User: Int){

            val management = EntitiesManagement() // should use an override here
            if(User == logged){

                val indexUser = getIndexUser(User)
                var member: UserMember

                if(indexUser != -1){

                    for(i in 0 until userList.size)
                        userList[i].followers.remove(User)

                    for(i in 0 until groupList.size){
                        member = UserMember(User,groupList[i].getId())
                        management.removeMember(member) // should use an override here
                    }

                    userList.remove(userList[indexUser])
                }
            }
        }

        fun createGroup(group: Group){

            group.updateCreator(getUserLogged())
            val indexCreator = getIndexUser(getUserLogged())

            if(group.getId() == -1 && verifyGroupName(group.getNameGroup())){
                group.updateId(generateIdUser())
                userList[indexCreator].groups.add(group.getId())
                if(getUserLogged() != -1 && getUserLogged()  == logged)
                    groupList.add(group)
            }
        }

        protected fun deleteGroup(member: UserMember){

            val indexGroup = getIndexGroup(member.group)

            if(indexGroup != -1 && groupList[indexGroup].getCreator() == member.id && member.id == logged){ //ONLY THE CREATOR OF THE GROUP CAN DELETE IT
                // percorre a lista de usuarios e remove todos que fazem parte do grupo
                for(i in 0 until userList.size)
                    userList[i].groups.remove(member.group) // removing the group id from the users profile

                groupList.remove(groupList[indexGroup])
            }
        }

        //AUTHENTICATION SYSTEM
        fun login(log: Login){
            val indexUser = getIndexUser(log.user)

            if(indexUser != -1 && userList[indexUser].getPass() == log.pass && logged == -1){
                auto.updateLoggedUser(log.user, -1)
                logged = log.user
            }

        }
        fun logoff(){
            if(logged != -1){
                auto.updateLoggedUser(-1, logged)
                logged = -1
            }
        }
        //AUTHENTICATION SYSTEM

    // STARTUP NECESSITIES



    //GENERATORS
        private fun generateIdUser(): Int{
            var id = Random.nextInt(1, 20000)

            for (i in userList){
                while(id == i.getId()) // enquanto novo ID existir ele gera diferentes
                    id = Random.nextInt(1, 20000)
            }
            return id
        }

        private fun generateIdGroup(): Int {
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
                if(userList[i].userName == name)
                    return false
            }
            return true
        }
        private fun verifyGroupName(name: String): Boolean {
            for(i in 0 until groupList.size){
                if(groupList[i].getNameGroup() == name)
                    return false
            }
            return true
        }
        protected fun verifyFollower(data: Follower): Int{
            val indexExternal = getIndexUser(data.external)

            for (i in 0 until userList[indexExternal].followers.size){
                if(userList[indexExternal].followers[i] == getUserLogged()){
                    return 1
                }
            }
            return 0
        }
    //VERIFIERS

    //INDEX FINDERS

        protected fun getIndexGroup(id: Int): Int {
            for(i in 0 until groupList.size) {
                if (groupList[i].getId() == id)
                    return i
            }
            return -1
        }

        protected fun getIndexUser(id: Int): Int {
            for(i in 0 until userList.size) {
                if (userList[i].getId() == id)
                    return i
            }
            return -1
        }

        protected fun getIndexMember(id: Int, idGroup: Int): Int {

            val indexGroup = getIndexGroup(idGroup)

            for(i in 0 until groupList[indexGroup].members.size) {
                if (groupList[indexGroup].members[i].user == id)
                    return i
            }
            return -1
        }

        protected fun getChatIndex(indexUser: Int, chatId: Int): Int{
            for(i in 0 until userList[indexUser].chat.size){
                if(userList[indexUser].chat[i].id == chatId)
                    return i
            }
            return -1
        }
    //INDEX FINDERS
}