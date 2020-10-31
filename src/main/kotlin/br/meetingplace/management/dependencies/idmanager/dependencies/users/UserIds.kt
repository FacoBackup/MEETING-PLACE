package br.meetingplace.management.dependencies.idmanager.dependencies.users

class UserIds private constructor(): UserIdsInterface {

    companion object{
        private val Class = UserIds()
        fun getClass() = Class
    }

    override fun fixEmail(email: String): String{
        return (email.replace("\\s".toRegex(),"")).toLowerCase()
    }

//    override fun attachNameToEmail(name: String, email: String): String{
//        return fixUserName(name)+"_"+email
//    }
//
//    override fun getEmailByAttachedNameToEmail(attached: String): String{
//        return attached.replaceBefore("_", "").removeSuffix("_")
//    }
//    override fun getNameByAttachedNameToEmail(attached: String): String{
//        return attached.replaceAfter("_", "").removeSuffix("_")
//    }
}