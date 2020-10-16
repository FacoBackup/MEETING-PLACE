package br.meetingplace.management.operations.finders

class GroupFinders {

    fun getGroupIndex(id: Int): Int {
        if(verifyGroup(id)){
            for(i in 0 until groupList.size) {
                if (groupList[i].getId() == id)
                    return i
            }
            return -1
        }
        else return -1
    }
    fun getMemberIndex(id: Int, idGroup: Int): Int {
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
}