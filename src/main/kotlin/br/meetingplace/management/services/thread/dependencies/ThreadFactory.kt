package br.meetingplace.management.services.thread.dependencies

import br.meetingplace.data.threads.ThreadData
import br.meetingplace.data.threads.ThreadOperationsData
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteCommunity
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteThread
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.community.Community

class ThreadFactory: Verify, ReadWriteUser, ReadWriteThread, ReadWriteCommunity{

    private fun verifyType(op: ThreadOperationsData?, data: ThreadData?): Int{
        return if(op != null && data == null){
            if(op.idSubThread.isNullOrBlank())
                0 //MAIN
            else
                1 //SUB
        }
        else if(data != null && op == null){
            if(data.idThread != null)
                1 //SUB
            else
                0 //MAIN
        }
        else -1
    }

    fun create(data: ThreadData){ //NEEDS WORK HERE
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        lateinit var community: Community

        if(verifyLoggedUser(user)){

            when(verifyType(null,data)){
                0->{ //MAIN
                    if(data.idCommunity.isNullOrBlank())
                        MainOperator.getMainThreadOperator().create(data)
                    else{
                        community = readCommunity(data.idCommunity)
                        if(verifyCommunity(community) ){
                            val idThread = MainOperator.getMainThreadOperator().create(data)
                            //the verification for data.idCommunity != null already occurred, so don't mind the !!
                            if(loggedUser !in community.getModerators())
                                community.threads.updateThreadsInValidation(idThread!!, null)
                            else
                                community.threads.updateThreadsInValidation(idThread!!, true)
                            writeCommunity(community, community.getId())
                        }
                    }
                }
                1->{ //SUB
                    if(data.idCommunity.isNullOrBlank())
                        SubOperator.getSubThreadOperator().create(data)
                    else{
                        community = readCommunity(data.idCommunity)
                        if(verifyCommunity(community) && community.threads.checkThreadApproval(data.idCommunity))
                            SubOperator.getSubThreadOperator().create(data)
                    }
                }
            }
        }
    }

    fun like(data: ThreadOperationsData){
        lateinit var community: Community

        when(verifyType(data,null)){
            0->{ //MAIN
                if(data.idCommunity.isNullOrBlank())
                    MainOperator.getMainThreadOperator().like(data)
                else{
                    community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.threads.checkThreadApproval(data.idCommunity))
                        MainOperator.getMainThreadOperator().like(data)
                }
            }
            1->{ //SUB
                if(data.idCommunity.isNullOrBlank())
                    SubOperator.getSubThreadOperator().like(data)
                else{
                    community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.threads.checkThreadApproval(data.idCommunity))
                        SubOperator.getSubThreadOperator().like(data)
                }
            }
        }
    }

    fun dislike(data: ThreadOperationsData){
        lateinit var community: Community

        when(verifyType(data,null)){
            0->{ //MAIN
                if(data.idCommunity.isNullOrBlank())
                    MainOperator.getMainThreadOperator().dislike(data)
                else{
                    community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.threads.checkThreadApproval(data.idCommunity))
                        MainOperator.getMainThreadOperator().dislike(data)
                }
            }
            1->{ //SUB
                if(data.idCommunity.isNullOrBlank())
                    SubOperator.getSubThreadOperator().dislike(data)
                else{
                    community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.threads.checkThreadApproval(data.idCommunity))
                        SubOperator.getSubThreadOperator().dislike(data)
                }
            }
        }
    }

    fun delete(data: ThreadOperationsData){
        lateinit var community: Community

        when(verifyType(data,null)){
            0->{ //MAIN
                if(data.idCommunity.isNullOrBlank())
                    MainOperator.getMainThreadOperator().delete(data)
                else{
                    community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.threads.checkThreadApproval(data.idCommunity)){
                        community.threads.removeApprovedThread(data.idThread)
                        MainOperator.getMainThreadOperator().delete(data)
                        writeCommunity(community, community.getId())
                    }
                    else if(verifyCommunity(community) && !community.threads.checkThreadApproval(data.idCommunity)){
                        community.threads.updateThreadsInValidation(data.idThread, false)
                        MainOperator.getMainThreadOperator().delete(data)
                        writeCommunity(community, community.getId())
                    }
                }
            }
            1->{ //SUB
                if(data.idCommunity.isNullOrBlank())
                    SubOperator.getSubThreadOperator().delete(data)
                else{
                    community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.threads.checkThreadApproval(data.idCommunity))
                        SubOperator.getSubThreadOperator().delete(data)
                    writeCommunity(community, community.getId())
                }
            }
        }
    }
}