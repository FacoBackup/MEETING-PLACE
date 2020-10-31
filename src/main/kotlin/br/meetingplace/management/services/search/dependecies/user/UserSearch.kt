package br.meetingplace.management.services.search.dependecies.user

import br.meetingplace.data.Data
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.management.services.search.dependecies.data.SimplifiedUser
import br.meetingplace.management.services.search.dependecies.type.SearchType
class UserSearch private constructor(): UserSearchInterface {
    private val iDs = IDsController.getClass()
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()

    companion object {
        private val Class = UserSearch()
        fun getClass() = Class
    }

    override fun searchUser(data: Data): List<SimplifiedUser>{
        if (!data.name.isNullOrBlank()) {
            val user = rw.readUser(iDs.attachNameToEmail(data.name, data.ID))
            return if (verify.verifyUser(user)) {
                listOf(SimplifiedUser(user.getUserName(), user.getEmail(), iDs.attachNameToEmail(user.getUserName(), user.getEmail())))
            } else listOf()
        }
        return listOf()
    }
}



//    override fun searchUser(data: Data): List<SimplifiedUser>{
//        when(verifyType(data)){
//            SearchType.BY_NAME->{
//
//            }
//            SearchType.BY_EMAIL->{
//                val user = rw.readUser(data.ID)
//                return if(verify.verifyUser(user)){
//                    listOf(SimplifiedUser(user.getUserName(), user.getEmail(), iDs.attachNameToEmail(user.getUserName(), user.getEmail())))
//                }
//                else listOf()
//            }
//        }
//        return listOf()
//    }
//
//    private fun verifyType(data: Data): SearchType?{
//        return if(data.ID.contains("@"))
//            SearchType.BY_EMAIL
//        else if (!data.ID.contains("@"))
//            SearchType.BY_NAME
//        else null
//    }
