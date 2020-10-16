package br.meetingplace.management.operations.verifiers

class GroupVerifiers private constructor(){

    companion object{
        private val verifier = GroupVerifiers()
        fun getGroupVerifier () = verifier
    }

    protected fun verifyGroupName(name: String): Boolean {
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

}